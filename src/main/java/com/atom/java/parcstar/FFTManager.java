package com.atom.java.parcstar;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FFTManager {
    public static ExecutorService service = Executors.newFixedThreadPool(8);

    public File convertToAudioFile(ByteBuffer bb) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd_HHmmss");
        File f = new File("src/main/resources/audio/" + dtf.format(LocalDateTime.now()) + ".wav");
        Files.write(Path.of(f.getPath()), bb.array());
        return f;
    }

}
