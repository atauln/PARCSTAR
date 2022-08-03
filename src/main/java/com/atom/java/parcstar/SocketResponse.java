package com.atom.java.parcstar;

public class SocketResponse {
    public int response_type;
    // 0 - 100 are reserved for client
    // 0 = User Authentication Details
    // 1 = Acknowledgement of information
    // 2 = Request to send WAV data
    // 3 = Request to confirm data integrity (include integrity value of current logs)

    //101 - 200 are reserved for server
    //101 = Request to send user authentication details
    //102 = Failed to parse JSON

    public String username; // 0

    public SocketResponse(int response_type) {
        this.response_type = response_type;
    }
}
