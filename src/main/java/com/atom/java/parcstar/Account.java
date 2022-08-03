package com.atom.java.parcstar;

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
}
