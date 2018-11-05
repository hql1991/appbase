package com.app.server.models;

import java.util.Date;

public class Rental {
    String id = null;

    String renterid;
    String ownerid;
    String rentalsd;
    String rentaled;
    String timestamp;


    public String getId() {
        return id;
    }

    public String getrenterid() {
        return renterid;
    }

    public String getownerid() {
        return ownerid;
    }

    public String getrentalsd() {
        return rentalsd;
    }

    public String getrentaled() {
        return rentaled;
    }

    public String gettimestamp() {
        return timestamp;
    }


    public Rental(String renterid, String ownerid, String rentalsd, String rentaled, String timestamp) {
        this.renterid = renterid;
        this.ownerid = ownerid;
        this.rentalsd = rentalsd;
        this.rentaled= rentaled;
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }
}
