package com.order.service.service;

import com.order.service.entity.Order;
import com.order.service.entity.OrderStatus;
import com.order.service.event.OrderEvent;
import com.order.service.kafka.OrderProducer;
import com.order.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repo;
    private final OrderProducer orderProducer;

    public Order create(Order order) {

        Order saved = repo.save(order);

        sendEvent(saved);

        return saved;
    }

    public Order confirm(Long id) {

        Order order = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.CONFIRMED);
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = repo.save(order);

        sendEvent(saved);

        return saved;
    }

    public Order cancel(Long id) {

        Order order = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = repo.save(order);

        sendEvent(saved);

        return saved;
    }

    public Order update(Long id, Order updated) {

        Order order = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setProductName(updated.getProductName());
        order.setAmount(updated.getAmount());
        order.setUserEmail(updated.getUserEmail());
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = repo.save(order);

        // 🔥 IMPORTANT: update event bhi bhejna
        sendEvent(saved);

        return saved;
    }

    public List<Order> getAll() {
        return repo.findAll();
    }

    public Order get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // 🔑 SINGLE RESPONSIBILITY METHOD (BEST PRACTICE)
    private void sendEvent(Order order) {

        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .productName(order.getProductName())
                .amount(order.getAmount())
                .userEmail(order.getUserEmail())
                .build();

        orderProducer.sendOrderEvent(event);
    }
}
