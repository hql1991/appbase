package com.app.server.models;

import java.util.Date;

public class Appointment {
    String id = null;

    String detail;
    String senderId;
    String receiverId;
    Date meetTime;
    boolean accepted;


    public String getId() {
        return id;
    }

    public String getDetail() {
        return detail;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public Date getMeetTime() {
        return meetTime;
    }

    public boolean isAccepted() {
        return accepted;
    }


    public Appointment(String detail, String senderId, String receiverId, Date meetTime, boolean accepted) {
        this.detail = detail;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.meetTime = meetTime;
        this.accepted = accepted;
    }

    public void setId(String id) {
        this.id = id;
    }
}
