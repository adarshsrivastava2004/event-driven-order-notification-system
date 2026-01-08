package com.notification.notificationservice.ai;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.notification.notificationservice.model.OrderEvent;

@Service
public class GeminiService implements AiService {

    private static final String GEMINI_URL =
    "https://generativelanguage.googleapis.com/v1beta/models/"
  + "gemini-1.0-pro:generateContent?key=";


    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = System.getenv("GEMINI_API_KEY");

    @Override
    public String generateOrderMessage(OrderEvent event) {

        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("❌ GEMINI_API_KEY missing");
            return fallback(event);
        }

        String prompt = buildPrompt(event);

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of(
                    "parts", List.of(
                        Map.of("text", prompt)
                    )
                )
            )
        );

        try {
            Map<?, ?> response = restTemplate.postForObject(
                GEMINI_URL + apiKey,
                requestBody,
                Map.class
            );

            System.out.println("🧠 Gemini raw response = " + response);

            return extractText(response);

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 Kafka consumer must not crash
            return fallback(event);
        }
    }

    private String buildPrompt(OrderEvent event) {
        return """
        You are an e-commerce notification assistant.
        Do NOT change facts.

        Facts:
        Order ID: %d
        Product: %s
        Status: %s
        Amount: %d

        Write a short, polite email message.
        """
        .formatted(
            event.getOrderId(),
            event.getProductName(),
            event.getStatus(),
            event.getAmount()
        );
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<?, ?> response) {

        try {
            List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) response.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return "Your order update is available.";
            }

            Map<String, Object> content =
                (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                (List<Map<String, Object>>) content.get("parts");

            if (parts == null || parts.isEmpty()) {
                return "Your order update is available.";
            }

            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            System.err.println("❌ Gemini response parsing failed");
            return "Your order update is available.";
        }
    }

    private String fallback(OrderEvent event) {
        return "Your order " + event.getOrderId()
               + " is currently " + event.getStatus() + ".";
    }
}
