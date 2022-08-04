package com.atom.java.parcstar;

import java.net.InetSocketAddress;

public class WebSocketThread extends Thread {
    public WebSocketManager wsm;

    public void run() {
        wsm = new WebSocketManager(new InetSocketAddress("localhost", 8887));
        wsm.run();
    }
}
