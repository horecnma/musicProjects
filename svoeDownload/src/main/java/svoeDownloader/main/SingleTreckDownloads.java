package svoeDownloader.main;

import java.io.InputStream;

import svoeDownloader.FileSaver;
import svoeDownloader.SingleTrackDownloader;

/**
 * @author mnikolaev
 */
public class SingleTreckDownloads
{
    public static void main(String[] args) throws Exception {
        FileSaver f = new FileSaver("/home/mnikolaev/temp/");
        SingleTrackDownloader t = new SingleTrackDownloader("/home/mnikolaev/temp/");
        String bandName = "Flёur";
        String trackName = "Взрывная Волна";
        String fileName = t.getFileNameUrl(bandName, trackName);
        InputStream inputStream = t.loadFile(fileName);
        f.saveFile(inputStream, bandName, trackName);
//        InputStream inputStream = svoeDownloader.loadFile(fileName);
//        inputStream.close();
    }
}
