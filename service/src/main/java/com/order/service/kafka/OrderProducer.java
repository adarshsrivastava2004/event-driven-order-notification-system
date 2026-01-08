package com.order.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.service.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendOrderEvent(OrderEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            kafkaTemplate.send("order-events", json);
            System.out.println("EVENT SENT → " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
