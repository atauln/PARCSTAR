package com.atom.java.parcstar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.util.ArrayList;

public class ServerDashboard extends JFrame {
    double[][] fftTimeData, webSocketLatencyTimeData = {};
    DefaultXYDataset fftTimes, webSocketLatencyTimes = new DefaultXYDataset();
    ChartPanel fftPanel, wsPanel;
    JFreeChart fftChart, wsChart;

    public ServerDashboard() {
        this.setResizable(false);
        this.setSize(600, 250);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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

    public void resetFFTTimes() {
        double[][] data = {};
        try {
            fftTimes.removeSeries("Times");
        } catch (Exception e) {
            e.printStackTrace();
        }
        fftTimes.addSeries("Times", data);
        fftTimeData = new double[][]{};
    }

    public void resetWebSocketLatencyTimes() {
        double[][] data = {};
        try {
            webSocketLatencyTimes.removeSeries("Times");
        } catch (Exception e) {
            e.printStackTrace();
        }
        webSocketLatencyTimes.addSeries("Times", data);
        webSocketLatencyTimeData = new double[][]{};
    }

    public void addFFTDataPoints(double[][] values) {
        if (values.length != 2) {
            System.err.println("Please ensure that you are providing only an X and a Y value");
            return;
        }
        double[][] data = fftTimeData;
        this.resetFFTTimes();
        ArrayList<ArrayList<Double>> tempData = new ArrayList<>();
        for (int i=0; i<2; i++) {
            for (int j=0; j<(data[0].length + values[0].length); j++) {
                if (j < data[0].length) {
                    tempData.get(i).set(j, data[i][j]);
                } else {
                    tempData.get(i).set(j, values[i][j - data[0].length]);
                }
            }
        }
        try {
            fftTimeData = (double[][]) tempData.toArray();
            fftTimes.removeSeries("Times");
            fftTimes.addSeries("Times", fftTimeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWSDataPoints(double[][] values) {
        if (values.length != 2) {
            System.err.println("Please ensure that you are providing only an X and a Y value");
            return;
        }
        double[][] data = webSocketLatencyTimeData;
        this.resetWebSocketLatencyTimes();
        ArrayList<ArrayList<Double>> tempData = new ArrayList<>();
        for (int i=0; i<2; i++) {
            for (int j=0; j<(data[0].length + values[0].length); j++) {
                if (j < data[0].length) {
                    tempData.get(i).set(j, data[i][j]);
                } else {
                    tempData.get(i).set(j, values[i][j - data[0].length]);
                }
            }
        }
        try {
            webSocketLatencyTimeData = (double[][]) tempData.toArray();
            webSocketLatencyTimes.removeSeries("Times");
            webSocketLatencyTimes.addSeries("Times", webSocketLatencyTimeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
