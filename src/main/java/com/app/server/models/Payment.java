package com.app.server.models;

public class Payment {

    public String getId() {
        return id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public String getRentalId() {
        return rentalId;
    }

    String id =null;
    String transactionId;
    String amount;
    String rentalId;

    public Payment(
            String transactionId,
            String amount,
            String rentalId

    ) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.rentalId = rentalId;


    }
    public void setId(String ownerid) {
        this.id = ownerid;
    }
}
