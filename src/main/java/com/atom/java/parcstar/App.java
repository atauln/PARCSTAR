package com.atom.java.parcstar;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class App {

    public ServerDashboard sd;
    public static WebSocketThread webSocketThread;
    public static WebSocketClient wsc;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        FileManager fm = new FileManager();
        fm.saveUserState(new Account("Ata", "Noor", "siratomxvii"));
        System.out.println(fm.retrieveUserState("siratomxvii").getDetails());

        webSocketThread = new WebSocketThread();
        webSocketThread.start();

        try {
            wsc = new WSClient( new URI("ws://localhost:8887/ws"));
            wsc.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
