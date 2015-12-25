package svoeDownloader;

import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * @author mnikolaev
 */
public class Main {
    public static void main(String[] args) throws Exception {
        FileSaver f = new FileSaver();
        TrackLoader t = new TrackLoader();
        String bandName = "Flёur";
        String trackName = "Взрывная Волна";
        String fileName = t.getFileName(bandName, trackName);
        InputStream inputStream = t.loadFile(fileName);
        f.saveFile(inputStream, bandName, trackName);
//        InputStream inputStream = svoeDownloader.loadFile(fileName);
//        inputStream.close();
    }
}
