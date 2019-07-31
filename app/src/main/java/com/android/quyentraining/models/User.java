package com.android.quyentraining.models;

public class User {
    private long timeactivity;
    private String username;
    private String typeaccount;
    private long timecreation;
    public User() {
    }

    public User(long timeactivity, String username, String typeaccount,long timecreation) {
        this.timeactivity = timeactivity;
        this.username = username;
        this.typeaccount = typeaccount;
        this.timecreation = timecreation;
    }

    public long getTimecreation() {
        return timecreation;
    }

    public long getTimeactivity() {
        return timeactivity;
    }

    public String getUsername() {
        return username;
    }

    public String getTypeaccount() {
        return typeaccount;
    }
}
