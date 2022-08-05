package com.atom.java.parcstar;

import com.google.gson.Gson;

public class SocketResponse {
    public int response_type;
    // 0 - 99 are reserved for client
    // 0 = User Authentication Details
    // 1 = Acknowledgement of information
    // 2 = Request to send WAV data
    // 3 = Request to confirm data integrity (include integrity value of current logs)
    // 4 = Request for user account data
    // 5 = Request to close connection

    //100 - 199 are reserved for server
    //100 = Acknowledgement of information
    //101 = Request to send user authentication details
    //102 = Failed to parse JSON
    //103 = Approve request
    //104 = Notification to maintain status

    public int getResponse_type() {
        return response_type;
    }

    public SocketResponse setResponse_type(int response_type) {
        this.response_type = response_type;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SocketResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public String username; // 0

    public SocketResponse(int response_type) {
        this.response_type = response_type;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
