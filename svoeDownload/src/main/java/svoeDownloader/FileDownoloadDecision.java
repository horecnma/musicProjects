package svoeDownloader;

import java.io.File;

/**
 *
 */
public class FileDownoloadDecision {
    public static final String[] ARCHIVES = new String[]{
            "C:\\RELAXATION\\radio\\fromSite\\",
            "C:\\RELAXATION\\radio\\fromSite\\2015_01_14_update\\",
            "C:\\RELAXATION\\radio\\fromSite\\2015_02_26_update\\",
            "C:\\RELAXATION\\radio\\fromSite\\2015_06_29_update\\",
            "C:\\RELAXATION\\radio\\fromSite\\2015_12_01_update\\",
            "C:\\RELAXATION\\radio\\fromSite\\2016_03_03_update\\",
            "C:\\RELAXATION\\radio\\fromSite\\trash\\",
            "c:\\temp\\"
    };

    public boolean shouldDownload(String bandName, String trackName) {
        return !exists(bandName.trim(), trackName.trim())
                && !bandName.contains("Программа ЖИВЫЕ");
    }

    private boolean exists(String bandName, String trackName) {
        boolean exists = false;
        for (String archive : ARCHIVES) {
            boolean existsInArchive = new File(FileSaver.getName(bandName, trackName, archive)).exists();
            if (existsInArchive) {
                exists = true;
            }
        }
        return exists;
    }
}
