package com.order.service.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent {

    private Long orderId;
    private String status;
    private String productName;
    private Double amount;
    private String userEmail;
}
