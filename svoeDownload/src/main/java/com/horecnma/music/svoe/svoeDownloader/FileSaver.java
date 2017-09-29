package com.horecnma.music.svoe.svoeDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author mnikolaev
 */
@Service
public class FileSaver {
    @Autowired
    @Qualifier("destination")
    public String destination;

    public void saveFile(InputStream inputStream, String bandName, String trackName) throws IOException {
        FileOutputStream output = new FileOutputStream(getName(bandName, trackName, destination));
        try {
            IOUtils.copy(inputStream, output);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static String getName(String bandName, String trackName, String destination) {
        return FilenameUtils.concat(destination, bandName + " - " + trackName + ".mp3");
    }

    public void delete(String bandName, String trackName) {
        File[] files = new File(destination).listFiles();
        for (File file : files) {
            if (file.getPath().replaceAll(" ", "").equals(getName(bandName, trackName, destination).replaceAll(" ", ""))) {
                System.out.println("удаление файла " + getName(bandName, trackName, destination));
                file.delete();
            }
        }

    }
}
