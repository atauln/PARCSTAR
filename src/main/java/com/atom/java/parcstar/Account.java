package com.atom.java.parcstar;

import java.util.Date;

public class Account {
    public String first_name, last_name;
    public Date account_created;
    public int id;

    public Account(String first_name, String last_name, int id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
    }
}
