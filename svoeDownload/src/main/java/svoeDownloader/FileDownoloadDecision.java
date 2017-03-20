package svoeDownloader;

import java.io.File;

import com.beust.jcommander.internal.Lists;

/**
 *
 */
public class FileDownoloadDecision {
    public static final String[] ARCHIVES = new String[]{
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

    public boolean shouldDownload(String bandName, String trackName) {
        return !exists(bandName.trim(), trackName.trim())
                && !bandName.contains("Программа ЖИВЫЕ");
    }

    private boolean exists(String bandName, String trackName) {
        return Lists.newArrayList(ARCHIVES).stream()
                    .anyMatch(archive -> new File(FileSaver.getName(bandName, trackName, archive)).exists());
    }
}
