package com.notification.notificationservice.ai;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.notification.notificationservice.model.OrderEvent;

@Service
public class GeminiService implements AiService {

    private static final Logger log =
            LoggerFactory.getLogger(GeminiService.class);

    
    private static final String GEMINI_URL =
         "https://generativelanguage.googleapis.com/v1beta/models/"
+ "gemini-2.5-flash:generateContent?key=";

    private final RestTemplate restTemplate;
    private final String apiKey;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
        this.apiKey = System.getenv("GEMINI_API_KEY");
    }

    @Override
    public String generateOrderMessage(OrderEvent event) {

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("GEMINI_API_KEY not found, using fallback message");
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

            return extractText(response);

        } catch (Exception ex) {
            
            log.error("Gemini API failed, using fallback", ex);
            return fallback(event);
        }
    }

    
    private String buildPrompt(OrderEvent event) {
        return """
        You are a notification assistant for an order system.
        Do not change or invent facts.

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

        if (response == null) {
            return fallbackText();
        }

        try {
            List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) response.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return fallbackText();
            }

            Map<String, Object> content =
                (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                (List<Map<String, Object>>) content.get("parts");

            if (parts == null || parts.isEmpty()) {
                return fallbackText();
            }

            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            return fallbackText();
        }
    }

    private String fallback(OrderEvent event) {
        return "Your order " + event.getOrderId()
               + " is currently " + event.getStatus() + ".";
    }

    private String fallbackText() {
        return "Your order update is available.";
    }
}
