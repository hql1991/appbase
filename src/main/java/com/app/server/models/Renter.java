package com.app.server.models;

public class Renter {

    String id = null;
    String firstName;
    String lastName;
    String email;
    int gender;
    int job;
    int num;
    int cook;
    int pref_gender;
    int pref_job;
    int pref_num;
    int pref_cook;
    String password;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getGender() {
        return gender;
    }

    public int getJob() {
        return job;
    }

    public int getNum() {
        return num;
    }

    public int getCook() {
        return cook;
    }

    public int getPref_gender() {
        return pref_gender;
    }

    public int getPref_job() {
        return pref_job;
    }

    public int getPref_num() {
        return pref_num;
    }

    public int getPref_cook() {
        return pref_cook;
    }

    public String getPassword() {
        return password;
    }

    public Renter(String firstName, String lastName, String email, int gender, int job, int num, int cook, int pref_gender, int pref_job, int pref_num, int pref_cook, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.job = job;
        this.num = num;
        this.cook = cook;
        this.pref_gender = pref_gender;
        this.pref_job = pref_job;
        this.pref_num = pref_num;
        this.pref_cook = pref_cook;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }
}
