package com.atom.java.parcstar;

import org.java_websocket.client.WebSocketClient;

import java.net.URI;


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

        Thread webClientThread = new Thread(() -> {
            try {
                wsc = new WSClient( new URI("ws://localhost:8887/ws"));
                Thread.sleep(1_000);
                wsc.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        webClientThread.start();
    }
}
