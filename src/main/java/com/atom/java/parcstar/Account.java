package com.atom.java.parcstar;

public class Account {
    public String first_name, last_name;
    public int id;

    public Account(String first_name, String last_name, int id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
    }

    public String getDetails() {
        return "Name: " + first_name + " " + last_name + "\n" +
                "ID: " + id;
    }
}
