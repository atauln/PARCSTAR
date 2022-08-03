package com.atom.java.parcstar;

import com.google.gson.Gson;

public class Account {
    public String first_name, last_name;
    public String username;

    public Account(String first_name, String last_name, String username) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
    }

    public String getDetails() {
        return "Name: " + first_name + " " + last_name + "\n" +
                "Username: " + username;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        new FileManager().saveUserState(this);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
