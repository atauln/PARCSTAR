package com.atom.java.parcstar;

import java.net.InetSocketAddress;

public class App {

    public ServerDashboard sd;
    public static WebSocketThread webSocketThread;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        FileManager fm = new FileManager();
        fm.saveUserState(new Account("Ata", "Noor", "siratomxvii"));
        System.out.println(fm.retrieveUserState("siratomxvii").getDetails());

        webSocketThread = new WebSocketThread();
        webSocketThread.start();
    }


}
