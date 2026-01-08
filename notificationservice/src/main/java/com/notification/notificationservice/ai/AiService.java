package com.notification.notificationservice.ai;

import com.notification.notificationservice.model.OrderEvent;

public interface AiService {
    String generateOrderMessage(OrderEvent event);
}
