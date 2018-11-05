package com.app.server.models;

import java.util.ArrayList;
import java.util.List;

public class RoommateRecommendation {
    String id = null;
    String renterId;
    List<String> roommate_recolist;

    public String getId() {
        return id;
    }

    public String getRenterId() {
        return renterId;
    }

    public List<String> getRoommate_recolist() {
        return roommate_recolist;
    }


    public RoommateRecommendation(String renterId, List<String> roommate_recolist) {
        this.renterId = renterId;
        this.roommate_recolist = roommate_recolist;
    }

    public void setId(String id) {
        this.id = id;
    }
}
