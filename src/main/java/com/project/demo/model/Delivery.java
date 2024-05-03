package com.project.demo.model;

public class Delivery {

    private int deliveryId;
    private String recipientName;
    private String address;
    private String deliveryDate;

    // Constructor
    public Delivery(int deliveryId, String recipientName, String address, String deliveryDate) {
        this.deliveryId = deliveryId;
        this.recipientName = recipientName;
        this.address = address;
        this.deliveryDate = deliveryDate;
    }

    // Getters and Setters
    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId=" + deliveryId +
                ", recipientName='" + recipientName + '\'' +
                ", address='" + address + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                '}';
    }
}

