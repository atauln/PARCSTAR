package com.atom.java.parcstar;

import org.java_websocket.WebSocket;
import org.quifft.QuiFFT;
import org.quifft.output.FFTResult;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FFTManager {
    public static ExecutorService service = Executors.newFixedThreadPool(16);
    public static HashMap<String, FFTResult> resultDirectory = new HashMap<>();
    public int packetsParsed = 0;

    public static File convertToAudioFile(ByteBuffer bb) throws IOException {
        File f = new File("src/main/resources/audio/" + System.nanoTime() + ".wav");
        Files.write(Path.of(f.getPath()), bb.array());
        return f;
    }

    public FFTResult getFFT(File f, ConnectionDetails cd) {
        Long time = System.nanoTime();

        service.submit(new Runnable() {
            @Override
            public void run() {
                cd.ffts.put(time, getFFTResult());
                try {
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                packetsParsed++;
            }

            public FFTResult getFFTResult() {
                try {
                    QuiFFT quiFFT = new QuiFFT(f).windowOverlap(0).numPoints((int) Math.pow(2, 16));
                    return quiFFT.fullFFT();
                } catch (UnsupportedAudioFileException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        while (!cd.ffts.containsKey(time)) {
            //just keep looping
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return cd.ffts.get(time);
    }
}