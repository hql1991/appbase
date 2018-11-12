package com.app.server.models;

public class Rental {
    String id = null;

    String renterId;
    String ownerId;
    String startDate;
    String endDate;
    String timeStamp;

    public String getId() {
        return id;
    }

    public String getRenterId() {
        return renterId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Rental(String renterId, String ownerId, String startDate, String endDate, String timeStamp) {
        this.renterId = renterId;
        this.ownerId = ownerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeStamp = timeStamp;
    }

    public void setId(String id) {
        this.id = id;
    }
}
