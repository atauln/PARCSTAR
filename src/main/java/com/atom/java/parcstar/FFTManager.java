package com.atom.java.parcstar;

import org.quifft.QuiFFT;
import org.quifft.output.FFTResult;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FFTManager {
    public static ExecutorService service = Executors.newFixedThreadPool(8);
    public static HashMap<String, FFTResult> resultDirectory = new HashMap<>();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd_HHmmss");

    public File convertToAudioFile(ByteBuffer bb) throws IOException {
        File f = new File("src/main/resources/audio/" + dtf.format(LocalDateTime.now()) + ".wav");
        Files.write(Path.of(f.getPath()), bb.array());
        return f;
    }

    public FFTResult getFFT(File f) {
        String time = dtf.format(LocalDateTime.now());
        service.submit(new Runnable() {
            @Override
            public void run() {
                FFTResult result = null;
                try {
                    QuiFFT quiFFT = new QuiFFT(f).windowOverlap(.25).numPoints((int) Math.pow(2, 14));
                    result = quiFFT.fullFFT();
                    resultDirectory.put(time, result);
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        while (!resultDirectory.containsKey(time)) {
            //just keep looping
        }

        return resultDirectory.get(time);
    }
}