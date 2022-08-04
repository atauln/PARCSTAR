package com.atom.java.parcstar;

import org.java_websocket.WebSocket;

public class DashboardThread extends Thread {
    public ServerDashboard sd;
    public WebSocket ws;

    public DashboardThread(WebSocket ws) {
        this.ws = ws;
    }

    public void run() {
        sd = new ServerDashboard(this.ws);
        sd.setVisible(true);
    }
}
