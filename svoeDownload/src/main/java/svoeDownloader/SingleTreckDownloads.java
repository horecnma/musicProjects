package svoeDownloader;

import java.io.InputStream;

/**
 * @author mnikolaev
 */
public class SingleTreckDownloads
{
    public static void main(String[] args) throws Exception {
        FileSaver f = new FileSaver();
        SingleTrackDownloader t = new SingleTrackDownloader();
        String bandName = "Flёur";
        String trackName = "Взрывная Волна";
        String fileName = t.getFileName(bandName, trackName);
        InputStream inputStream = t.loadFile(fileName);
        f.saveFile(inputStream, bandName, trackName);
//        InputStream inputStream = svoeDownloader.loadFile(fileName);
//        inputStream.close();
    }
}
