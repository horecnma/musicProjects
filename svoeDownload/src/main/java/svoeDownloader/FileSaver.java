package svoeDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author mnikolaev
 */
public class FileSaver {
    public static final String DESTINATION = "/home/mnikolaev/temp/";

    public void saveFile(InputStream inputStream, String bandName, String trackName) throws IOException {
        FileOutputStream output = new FileOutputStream(getName(bandName, trackName, DESTINATION));
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
        File[] files = new File(DESTINATION).listFiles();
        for (File file : files) {
            if (file.getPath().replaceAll(" ", "").equals(getName(bandName, trackName, DESTINATION).replaceAll(" ", ""))) {
                System.out.println("удаление файла " + getName(bandName, trackName, DESTINATION));
                file.delete();
            }
        }

    }
}
