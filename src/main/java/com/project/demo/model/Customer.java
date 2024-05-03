package com.project.demo.model;

public class Customer {
    private int id;
    private String fullName;
    private String contactInfo;

    // Constructors, getters, and setters

    public Customer() {
    }

    public Customer(int cust_id, String fullName, String contactInfo) {
        this.id=cust_id;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
    }

    public Customer(String fullName, String contactInfo) {
        this.fullName = fullName;
        this.contactInfo = contactInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
 
    

