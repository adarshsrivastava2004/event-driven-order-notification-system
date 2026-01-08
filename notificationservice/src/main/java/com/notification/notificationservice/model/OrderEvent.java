package com.notification.notificationservice.model;

public class OrderEvent {

    private Long orderId;
    private String status;
    private String productName;
    private Long amount;
    private String userEmail;

    public OrderEvent() {}

    public OrderEvent(Long orderId, String status, String productName,
                      Long amount, String userEmail) {
        this.orderId = orderId;
        this.status = status;
        this.productName = productName;
        this.amount = amount;
        this.userEmail = userEmail;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
