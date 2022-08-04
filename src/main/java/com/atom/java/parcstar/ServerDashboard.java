package com.atom.java.parcstar;

import org.java_websocket.WebSocket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class ServerDashboard extends JFrame {
    double[][] fftTimeData, webSocketLatencyTimeData;
    DefaultXYDataset fftTimes, webSocketLatencyTimes = new DefaultXYDataset();
    ChartPanel fftPanel, wsPanel;
    JFreeChart fftChart, wsChart;
    WebSocket ws;

    public ServerDashboard(WebSocket ws) {
        this.ws = ws;
        this.setResizable(false);
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        WindowListener listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ws.close(200, "Connection closed by server");
                super.windowClosing(e);
            }
        };
        this.addWindowListener(listener);

        this.setLayout(new GridLayout(2, 1));
        this.fftTimes = new DefaultXYDataset();
        this.webSocketLatencyTimes = new DefaultXYDataset();
        this.setUpDisplay();
    }

    public ServerDashboard setUpDisplay() {
        fftChart = ChartFactory.createXYLineChart("FFT Times", "Job #", "Time (ms)",
                fftTimes, PlotOrientation.VERTICAL, true, true, false);
        wsChart = ChartFactory.createXYLineChart("WebSocket Latency", "Time Pinged (s)", "Time (ms)",
                webSocketLatencyTimes, PlotOrientation.VERTICAL, true, true, false);
        fftPanel = new ChartPanel(fftChart);
        wsPanel = new ChartPanel(wsChart);
        this.getContentPane().add(fftPanel);
        this.getContentPane().add(wsPanel);
        return this;
    }

    public ServerDashboard refreshDisplay() {
        fftChart = ChartFactory.createXYLineChart("FFT Times", "Job #", "Time (ms)",
                fftTimes, PlotOrientation.VERTICAL, true, true, false);
        wsChart = ChartFactory.createXYLineChart("WebSocket Latency", "Time Pinged (s)", "Time (ms)",
                webSocketLatencyTimes, PlotOrientation.VERTICAL, true, true, false);
        wsPanel.repaint();
        fftPanel.repaint();
        return this;
    }

    public void resetFFTTimes() {
        double[][] data = {{0}, {0}};
        try {
            fftTimes.removeSeries("Times");
        } catch (Exception e) {
            e.printStackTrace();
        }
        fftTimes.addSeries("Times", data);
        fftTimeData = data;
    }

    public void resetWebSocketLatencyTimes() {
        double[][] data = {{0}, {0}};
        try {
            webSocketLatencyTimes.removeSeries("Times");
        } catch (Exception e) {
            e.printStackTrace();
        }
        webSocketLatencyTimes.addSeries("Times", data);
        webSocketLatencyTimeData = data;
    }

    public void addFFTDataPoints(double[][] values) {
        if (values.length != 2) {
            System.err.println("Please ensure that you are providing only an X and a Y value");
            return;
        }
        if (fftTimeData == null) {
            this.resetFFTTimes();
        }
        ArrayList<ArrayList<Double>> tempData = new ArrayList<>();
        for (int i=0; i<2; i++) {
            tempData.add(new ArrayList<Double>());
            for (int j=0; j<(fftTimeData[0].length + values[0].length); j++) {
                if (j < fftTimeData[0].length) {
                    tempData.get(i).add(fftTimeData[i][j]);
                } else {
                    tempData.get(i).add(values[i][j - fftTimeData[0].length]);
                }
            }
        }
        this.resetFFTTimes();
        try {
            fftTimeData = new double[2][tempData.get(0).size()];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < tempData.get(0).size(); j++) {
                    fftTimeData[i][j] = tempData.get(i).get(j);
                }
            }
            fftTimes.removeSeries("Times");
            fftTimes.addSeries("Times", fftTimeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshDisplay();
    }

    public void addWSDataPoints(double[][] values) {
        if (values.length != 2) {
            System.err.println("Please ensure that you are providing only an X and a Y value");
            return;
        }
        if (webSocketLatencyTimeData == null) {
            this.resetWebSocketLatencyTimes();
        }
        ArrayList<ArrayList<Double>> tempData = new ArrayList<>();
        for (int i=0; i<2; i++) {
            tempData.add(new ArrayList<Double>());
            for (int j=0; j<(webSocketLatencyTimeData[0].length + values[0].length); j++) {
                if (j < webSocketLatencyTimeData[0].length) {
                    tempData.get(i).add(webSocketLatencyTimeData[i][j]);
                } else {
                    tempData.get(i).add(values[i][j - webSocketLatencyTimeData[0].length]);
                }
            }
        }
        this.resetWebSocketLatencyTimes();
        try {
            webSocketLatencyTimeData = new double[2][tempData.get(0).size()];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < tempData.get(0).size(); j++) {
                    webSocketLatencyTimeData[i][j] = tempData.get(i).get(j);
                }
            }
            webSocketLatencyTimes.removeSeries("Times");
            webSocketLatencyTimes.addSeries("Times", webSocketLatencyTimeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshDisplay();
    }
}
