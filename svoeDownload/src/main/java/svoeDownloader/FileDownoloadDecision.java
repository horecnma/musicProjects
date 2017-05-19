package svoeDownloader;

import java.io.File;

import com.beust.jcommander.internal.Lists;

/**
 *
 */
public class FileDownoloadDecision {
    public final String[] archives;

    public FileDownoloadDecision(String[] archives) {
        this.archives = archives;
    }

    public boolean shouldDownload(String bandName, String trackName) {
        return !exists(bandName.trim(), trackName.trim())
                && !bandName.contains("Программа ЖИВЫЕ");
    }

    private boolean exists(String bandName, String trackName) {
        return Lists.newArrayList(archives).stream()
                    .anyMatch(archive -> new File(FileSaver.getName(bandName, trackName, archive)).exists());
    }
}
