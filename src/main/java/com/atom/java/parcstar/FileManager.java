package com.atom.java.parcstar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

public class FileManager {

    public static void main(String[] args) {
        FileManager fm = new FileManager();
        fm.buildUser(new Account("Ataul", "Noor", 15));
    }

    public void buildUser(Account a){
        File f = new File("src/main/resources/userStats/" + a.id + ".json");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ioE) {
                ioE.printStackTrace();
            }
        } else {
            f.delete();
            try {
                f.createNewFile();
            } catch (IOException ioE) {
                ioE.printStackTrace();
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        try {
            Files.write(f.toPath(), Collections.singleton(gson.toJson(a)));
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }
}
