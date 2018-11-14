package com.app.server.models;

import java.util.ArrayList;
import java.util.List;

public class HouseRecommendation {
    String id = null;
    String renterId;
    List<String> house_recolist;

    public String getId() {
        return id;
    }

    public String getrenterId() {
        return renterId;
    }

    public List<String> getHouse_recolist() {
        return house_recolist;
    }


    public HouseRecommendation(String renterId, List<String> house_recolist) {
        this.renterId = renterId;
        this.house_recolist = house_recolist;
    }

    public void setId(String id) {
        this.id = id;
    }
}