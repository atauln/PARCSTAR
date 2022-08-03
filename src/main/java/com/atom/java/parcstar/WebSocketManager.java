package com.atom.java.parcstar;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class WebSocketManager extends WebSocketServer {
    FFTManager fftManager = new FFTManager();
    HashMap<InetSocketAddress, ConnectionDetails> connections = new HashMap<>();

    //ALL MESSAGES TO THIS SERVER MUST BE IN .JSON FORMAT

    public WebSocketManager(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        webSocket.send("Connection opened!");
        System.out.println("Client connected at: " + webSocket.getRemoteSocketAddress());
        System.out.println(clientHandshake.getResourceDescriptor());
        connections.put(webSocket.getRemoteSocketAddress(), new ConnectionDetails(null, webSocket.getRemoteSocketAddress()));
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Connection closed!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("Message received from " + webSocket.getRemoteSocketAddress() + ": " + s);
        webSocket.send("Message received!");
        ConnectionDetails cd = connections.get(webSocket.getRemoteSocketAddress());
        if (cd == null) {
            connections.put(webSocket.getRemoteSocketAddress(), new ConnectionDetails(null, webSocket.getRemoteSocketAddress()));
        }
        SocketResponse sr = null;
        try {
            sr = new Gson().fromJson(s, SocketResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            SocketResponse sa = new SocketResponse(102);
            cd.addResponse(true, sa);
            webSocket.send(new Gson().toJson(sa));
        }
        cd.addResponse(false, sr);
        int responseCode = sr.response_type;
        if (cd.getUsername() == null && sr.response_type != 0) {
            webSocket.send(new Gson().toJson(new SocketResponse(101)));
            //TODO log server sent ^^^
            return;
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        //on audio received
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
