package com.atom.java.parcstar;

import org.java_websocket.WebSocket;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class ConnectionDetails {
    public WebSocket ws;
    public ArrayList<Object[]> srList = new ArrayList<>();
    // [boolean false (client sent) true (server sent), SocketResponse response]
    public Account account;
    public int pingNum = 0;
    public DashboardThread dashboardThread;

    public ConnectionDetails(String username, WebSocket ws) {
        this.ws = ws;
        this.account = new FileManager().retrieveUserState(username);
        if (account == null) {
            this.account = new Account(null, null, username);
            new FileManager().saveUserState(this.account);
        }
    }

    public ConnectionDetails(String username, WebSocket ws, ArrayList<Object[]> srList) {
        this.ws = ws;
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

    public void startThread() {
        dashboardThread = new DashboardThread(this.ws);
        this.dashboardThread.start();
    }
}
