package com.app.server.models;

public class House {

    public String getId() {
        return id;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public String getaddress() {
        return address;
    }

    public String getstate() {
        return state;
    }

    public String getzipcode() {
        return zipcode;
    }

    public String getyear() {
        return year;
    }



    String id =null;
    String ownerid;
    String address;
    String state;
    String zipcode;
    String year;

    public House(
            String ownerid,
            String address,
            String state,
            String zipcode,
            String year
    ) {
        this.ownerid = ownerid;
        this.address = address;
        this.state = state;
        this.zipcode = zipcode;
        this.year = year;

    }
    public void setId(String houseid) {
        this.id = houseid;
    }
}
