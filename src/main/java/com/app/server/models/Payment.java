package com.app.server.models;

public class Payment {

    public String getId() {
        return id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getRentalId() {
        return rentalId;
    }

    String id = null;
    String transactionId;
    Double amount;
    String rentalId;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Payment(String transactionId, Double amount, String rentalId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.rentalId = rentalId;
    }

    public void setId(String ownerid) {
        this.id = ownerid;
    }
}
