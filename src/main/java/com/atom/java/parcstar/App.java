package com.atom.java.parcstar;

import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) {
        FileManager fm = new FileManager();
        fm.saveUserState(new Account("Ata", "Noor", "siratomxvii"));
        System.out.println(fm.retrieveUserState("siratomxvii").getDetails());
        WebSocketManager wsm = new WebSocketManager(new InetSocketAddress("localhost", 8887));
        wsm.run();
    }


}
