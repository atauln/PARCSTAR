package com.atom.java.parcstar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class FileManager {

    public static void main(String[] args) {
        FileManager fm = new FileManager();
        fm.saveUserState(new Account("Ataul", "Noor", 13));
        System.out.println(fm.retrieveUserState(13).getDetails());
        WebSocketManager wsm = new WebSocketManager(new InetSocketAddress("localhost", 8887));
        wsm.run();
    }

    public void saveUserState(Account a) {
        File f = new File("src/main/resources/userStats/" + a.id + ".json");
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            Files.write(f.toPath(), Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(a)));
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }

    public Account retrieveUserState(int id) {
        try {
            File f = new File("src/main/resources/userStats/" + id + ".json");
            if (f.exists()) {
                return new Gson().fromJson(Files.readString(Path.of(f.getPath())), Account.class);
            }
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
        return null;
    }
}
