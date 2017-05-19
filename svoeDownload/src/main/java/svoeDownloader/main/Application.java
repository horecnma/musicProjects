package svoeDownloader.main;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

import svoeDownloader.AllTracksDownloader;

/**
 * @author Mikhail
 */
public class Application {

    private static final String DESTINATION = "/home/mnikolaev/temp/";
    private static final String[] ARCHIVES = {
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/",
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_01_14_update",
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_02_26_update",
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_06_29_update",
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_12_01_update",
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2016_03_03_update",
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2016_07_13_update",
            "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2017_01_13_update",
            "/home/mnikolaev/yandex.Disk/я_radio/trash/",
            "/home/mnikolaev/temp/"
    };

    public static void main(String[] args)
            throws IOException {
        BasicConfigurator.configure();
        AllTracksDownloader allTracksDownloader = new AllTracksDownloader(DESTINATION, ARCHIVES);
        allTracksDownloader.downloadAllTracks();
    }
}
