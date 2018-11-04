package com.app.server.models;

public class Owner {

    public String getOwnerid() {
        return ownerid;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public String getPrefGender() {
        return prefGender;
    }

    public String getPrefJob() {
        return prefJob;
    }

    public int getPrefNum() {
        return prefNum;
    }

    public String getPrefCook() { return prefCook; }


    String ownerid=null;
    String userName;
    String firstName;
    String lastName;
    String prefGender;
    String prefJob;
    int prefNum;
    String prefCook;

    public Owner(
            String userName,
            String firstName,
            String lastName,
            String prefGender,
            String prefJob,
            int prefNum,
            String prefCook
    ) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefGender = prefGender;
        this.prefJob = prefJob;
        this.prefNum = prefNum;
        this.prefCook = prefCook;

    }
    public void setId(String ownerid) {
        this.ownerid = ownerid;
    }
}
