package com.atom.java.parcstar;

public class DashboardThread extends Thread {
    public ServerDashboard sd;

    public void run() {
        sd = new ServerDashboard();
        sd.setVisible(true);
    }
}
