package com.atom.java.parcstar;

import org.java_websocket.WebSocket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.quifft.output.FFTResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class ServerDashboard extends JFrame {
    double[][] fftTimeData, webSocketLatencyTimeData = new double[][] {{0}, {0}};
    DefaultXYDataset fftTimes, webSocketLatencyTimes, fftDisplayTimes = new DefaultXYDataset();
    ChartPanel fftPanel, wsPanel, fftDPanel;
    JFreeChart fftChart, wsChart, fftDChart;
    WebSocket ws;
    ArrayList<ArrayList<Double>> fftToAdd = new ArrayList<>();

    public ServerDashboard(WebSocket ws) {
        this.ws = ws;
        this.setResizable(false);
        this.setSize(1200, 600);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        WindowListener listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ws.close(200, "Connection closed by server");
                super.windowClosing(e);
            }
        };
        this.addWindowListener(listener);

        this.setLayout(new GridLayout(2, 2));
        this.setTitle(ws.getRemoteSocketAddress().getAddress().toString());
        this.fftTimes = new DefaultXYDataset();
        this.webSocketLatencyTimes = new DefaultXYDataset();

        fftToAdd.add(new ArrayList<Double>());
        fftToAdd.add(new ArrayList<Double>());

        resetFFTTimes(new double[][]{{0.0}, {0.0}});
        resetWebSocketLatencyTimes(new double[][]{{0.0}, {0.0}});
        resetFFTDisplayChart(new double[][]{{0.0}, {0.0}});

        this.setUpDisplay();
    }

    public ServerDashboard setUpDisplay() {
        JLabel label = new JLabel();
        label.setText("Client: " + ws.getRemoteSocketAddress());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        fftChart = ChartFactory.createXYLineChart("FFT Times", "Job #", "Time (ms)",
                fftTimes, PlotOrientation.VERTICAL, true, true, false);
        wsChart = ChartFactory.createXYLineChart("WebSocket Latency", "Time Pinged (s)", "Time (ms)",
                webSocketLatencyTimes, PlotOrientation.VERTICAL, true, true, false);
        fftDChart = ChartFactory.createXYLineChart("FFT Monitor", "Note Half Step Deviation from A4", "Amplitude (dB)",
                fftDisplayTimes, PlotOrientation.VERTICAL, true, true, false);
        fftPanel = new ChartPanel(fftChart);
        wsPanel = new ChartPanel(wsChart);
        fftDPanel = new ChartPanel(fftDChart);
        this.getContentPane().add(label);
        this.getContentPane().add(fftPanel);
        this.getContentPane().add(wsPanel);
        this.getContentPane().add(fftDPanel);
        return this;
    }

    public ServerDashboard refreshDisplay() {
        fftChart = ChartFactory.createXYLineChart("FFT Times", "Job #", "Time (ms)",
                fftTimes, PlotOrientation.VERTICAL, true, true, false);
        wsChart = ChartFactory.createXYLineChart("WebSocket Latency", "Time Pinged (s)", "Time (ms)",
                webSocketLatencyTimes, PlotOrientation.VERTICAL, true, true, false);
        fftDChart = ChartFactory.createXYLineChart("FFT Monitor", "Note Half Step Deviation from A4", "Amplitude (dB)",
                fftDisplayTimes, PlotOrientation.VERTICAL, true, true, false);
        wsPanel.repaint();
        fftPanel.repaint();
        fftDPanel.repaint();
        return this;
    }

    public void resetFFTTimes(double[][] data) {
        try {
            fftTimes.removeSeries("Times");
            fftTimes.addSeries("Times", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fftTimeData = data;
    }

    public void resetWebSocketLatencyTimes(double[][] data) {
        try {
            webSocketLatencyTimes.removeSeries("Times");
            webSocketLatencyTimes.addSeries("Times", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        webSocketLatencyTimeData = data;
    }

    public void resetFFTDisplayChart(double[][] data) {
        try {
            fftDisplayTimes.removeSeries("Amplitude");
            fftDisplayTimes.addSeries("Amplitude", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeFFTDisplayPoints(FFTResult fftResult) {
        double[][] data = new double[][]{new double[49], new double[49]};
        for (int i = -31; i < 18; i++) {
            data[0][i + 31] = i;
            double fftFreq = fftResult.frequencyResolution;
            int index = (int) Math.round(440.0 * Math.pow((Math.pow(2, 1/12)), i) / fftFreq);
            data[1][i + 31] = fftResult.fftFrames[0].bins[index].amplitude;
        }
        resetFFTDisplayChart(data);
        refreshDisplay();
    }

    public void addFFTDataPoints(double val1, double val2) {
        fftToAdd.get(0).add(val1);
        fftToAdd.get(1).add(val2);
        if (fftToAdd.get(0).size() >= 6) {
            //Move FFT values that need to be added to a new variable and clear the old one, to make room for more values
            ArrayList<ArrayList<Double>> newValues = fftToAdd;

            for (int i = 0; i < 2; i++) {
                for (int j = fftTimeData[i].length - 1; j > 0; j--) {
                    newValues.get(i).add(0, fftTimeData[i][j]);
                }

                if (newValues.get(i).size() > 100) {
                    for (int j = 0; j < newValues.get(i).size() - 100; j++) {
                        newValues.get(i).remove(j);
                    }
                }

                fftTimeData[i] = new double[newValues.get(i).size()];

                for (int j = 0; j < newValues.get(i).size(); j++) {
                    fftTimeData[i][j] = newValues.get(i).get(j);
                }
            }
            resetFFTTimes(fftTimeData);
            refreshDisplay();
            fftToAdd.get(0).clear();
            fftToAdd.get(1).clear();
        }
    }

    public void addWSDataPoints(double[][] values) {
        if (values.length != 2) {
            System.err.println("Please ensure that you are providing only an X and a Y value");
            return;
        }
        if (webSocketLatencyTimeData == null) {
            this.resetWebSocketLatencyTimes(new double[][]{{0}, {0}});
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
        this.resetWebSocketLatencyTimes(new double[][]{{0}, {0}});
        if (tempData.get(0).size() > 100) {
            tempData.get(0).remove(0);
            tempData.get(1).remove(0);
        }
        try {
            webSocketLatencyTimeData = new double[2][tempData.get(0).size()];
            for (int i = 0; i < 2; i++) {
                for (int j = (tempData.get(0).size() > 100 ? tempData.get(0).size() - 100 : 0); j < tempData.get(0).size(); j++) {
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
