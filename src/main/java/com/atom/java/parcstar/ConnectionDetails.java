package com.atom.java.parcstar;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class ConnectionDetails {
    public String username;
    public InetSocketAddress address;
    public ArrayList<Object[]> srList = new ArrayList<>();
    // [boolean false (client sent) true (server sent), SocketResponse response]

    public ConnectionDetails(String username, InetSocketAddress address) {
        this.username = username;
        this.address = address;
    }

    public ConnectionDetails(String username, InetSocketAddress address, ArrayList<Object[]> srList) {
        this.username = username;
        this.address = address;
        this.srList = srList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Object[]> getResponseList() {
        return srList;
    }

    public void setResponseList(ArrayList<Object[]> srList) {
        this.srList = srList;
    }

    public void addResponse(boolean serverSent, SocketResponse sr) {
        srList.add(new Object[] {serverSent, sr});
    }
}
