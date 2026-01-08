package com.notification.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.notification.notificationservice.ai.AiService;
import com.notification.notificationservice.email.EmailService;
import com.notification.notificationservice.model.OrderEvent;

@Service
public class OrderEventConsumer {

    private final AiService aiService;
    private final EmailService emailService;

    public OrderEventConsumer(AiService aiService,
                              EmailService emailService) {
        this.aiService = aiService;
        this.emailService = emailService;
    }

    @KafkaListener(
        topics = "order-events",
        groupId = "notification-group"
    )
    public void consume(OrderEvent event) {

        System.out.println("📥 Event Received for Order: " + event.getOrderId());

        String aiMessage = aiService.generateOrderMessage(event);

        emailService.sendEmail(
            event.getUserEmail(),
            "Order Update - " + event.getOrderId(),
            aiMessage
        );
    }
}
