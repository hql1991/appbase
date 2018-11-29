package com.app.server.models;

public class Owner {

    public String getId() {
        return id;
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

    public String getPrefCook() {
        return prefCook;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }

    String id = null;
    String userName;
    String firstName;
    String lastName;
    String prefGender;
    String prefJob;
    int prefNum;
    String prefCook;
    String email;
    String password;
    byte[] salt;

    public Owner(
            String userName,
            String firstName,
            String lastName,
            String prefGender,
            String prefJob,
            int prefNum,
            String prefCook,
            String email,
            String password,
            byte[] salt
    ) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefGender = prefGender;
        this.prefJob = prefJob;
        this.prefNum = prefNum;
        this.prefCook = prefCook;
        this.email = email;
        this.password = password;
        this.salt=salt;
    }

    public void setId(String ownerid) {
        this.id = ownerid;
    }
}
