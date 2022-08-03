package com.atom.java.parcstar;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class ConnectionDetails {
    public InetSocketAddress address;
    public ArrayList<Object[]> srList = new ArrayList<>();
    // [boolean false (client sent) true (server sent), SocketResponse response]
    public Account account;

    public ConnectionDetails(String username, InetSocketAddress address) {
        this.address = address;
        this.account = new FileManager().retrieveUserState(username);
        if (account == null) {
            this.account = new Account(null, null, username);
            new FileManager().saveUserState(this.account);
        }
    }

    public ConnectionDetails(String username, InetSocketAddress address, ArrayList<Object[]> srList) {
        this.address = address;
        this.srList = srList;
        this.account = new FileManager().retrieveUserState(username);
        if (account == null) {
            this.account = new Account(null, null, username);
            new FileManager().saveUserState(this.account);
        }
    }

    public ArrayList<Object[]> getResponseList() {
        return srList;
    }

    public void setResponseList(ArrayList<Object[]> srList) {
        this.srList = srList;
    }

    public SocketResponse addResponse(boolean serverSent, SocketResponse sr) {
        srList.add(new Object[] {serverSent, sr});
        return sr;
    }
}
