package com.project.demo.model;

public class DeliveryStatus {

    private int orderId;
    private String status;

    // Constructor, getters, and setters

    public DeliveryStatus(int orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
