package com.atom.java.parcstar;

import com.google.gson.Gson;
import org.apache.commons.lang3.time.StopWatch;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class WebSocketManager extends WebSocketServer {
    FFTManager fftManager = new FFTManager();
    HashMap<InetSocketAddress, ConnectionDetails> connections = new HashMap<>();
    StopWatch sw = new StopWatch();

    //ALL MESSAGES TO AND FROM THIS SERVER MUST BE IN .JSON FORMAT

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
            webSocket.send(cd.addResponse(true, new SocketResponse(102)).toString());
            return;
        }
        cd.addResponse(false, sr);
        int responseCode = sr.response_type;
        if (cd.account.getUsername() == null && sr.response_type != 0) {
            webSocket.send(cd.addResponse(true, new SocketResponse(101)).toString());
            return;
        }
        switch (sr.response_type) {
            case 0: {
                cd.account.setUsername(sr.username);
                break;
            }
            case 1: {
                System.out.println(cd.account.username + " (" + webSocket.getRemoteSocketAddress() + ") has acknowledged receipt of message.");
                break;
            }
            case 2: {
                //TODO Request to send WAV data
                webSocket.send(cd.addResponse(true, new SocketResponse(103)).toString());
                break;
            }
            case 3: {
                //TODO Confirm data integrity
                break;
            }
            case 4: {
                webSocket.send(cd.account.toString());
                break;
            }
        }

    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
        super.onWebsocketPong(conn, f);
        System.out.println("Received pong from: " + conn.getRemoteSocketAddress() + ": " + f.toString());
        sw.stop();
        System.out.println("Time Elapsed:" + sw.getTime() + "ms");
        sw.reset();
    }

    @Override
    public PingFrame onPreparePing(WebSocket conn) {
        sw.start();
        return super.onPreparePing(conn);
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
        conn.sendPing();
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.err.println("An error occurred on connection " + webSocket.getRemoteSocketAddress() + ": " + e.toString());
        webSocket.send(connections.get(webSocket.getRemoteSocketAddress()).addResponse(true, new SocketResponse(103)).toString());
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully!");
    }
}
