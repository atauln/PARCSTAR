package com.atom.java.parcstar;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URI;

public class WSClient extends WebSocketClient {

    public WSClient(URI serverUri) {
        super(serverUri);
    }
    public int step = 1;
    public int packetsSent = 0;
    public boolean started = false;

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        this.send(new SocketResponse(0)
                .setUsername("SirAtomXVII")
                .toString());
    }

    @Override
    public void onMessage(String s) {
        if (started) {
            return;
        }
        started = true;
        Thread sa = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(15_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (step) {
                    case 1 -> send(new SocketResponse(2).toString());
                    case 2 -> {
                        Thread t = new Thread(() -> {
                            while (true) {
                                try {
                                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                                    BufferedInputStream in = new BufferedInputStream(new FileInputStream("src/main/resources/test_cut.wav"));

                                    int read;
                                    byte[] buff = new byte[1024];
                                    while ((read = in.read(buff)) > 0) {
                                        out.write(buff, 0, read);
                                    }
                                    out.flush();
                                    byte[] audioBytes = out.toByteArray();
                                    send(audioBytes);
                                    packetsSent++;
                                    Thread.sleep(50);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.start();
                    }
                }
                step++;
            }
        });
        sa.start();
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }


}
