package com.atom.java.parcstar;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class WebSocketManager extends WebSocketServer {
    FFTManager fftManager = new FFTManager();

    //ALL MESSAGES TO THIS SERVER MUST BE IN .JSON FORMAT

    public WebSocketManager(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        webSocket.send("Connection opened!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Connection closed!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("Message received from " + webSocket.getRemoteSocketAddress() + ": " + s);
        webSocket.send("Message received!");
        SocketResponse sr = new SocketResponse();
        try {
            sr = new Gson().fromJson(s, SocketResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sr.response_type == "") {
            //do stuff
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        //on audio recieved
        conn.send("Received byte buffer!");
        try {
            fftManager.getFFT(fftManager.convertToAudioFile(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.send("Completed job!");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.err.println("An error occurred on connection " + webSocket.getRemoteSocketAddress() + ": " + e.toString());
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully!");
    }
}
