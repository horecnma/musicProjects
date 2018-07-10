package com.horecnma.music.svoe.svoeDownloader;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.horecnma.music.svoe.svoeDownloader.dto.Track;

/**
 *
 */
@Service
public class FileDownoloadDecision {
    private final Set<String> downloadedTracks = new HashSet<>();

    @Autowired
    @Qualifier("archives")
    void setArchives(String[] archives){
        for (File archiveDir : Lists.newArrayList(archives).stream().map(File::new).collect(Collectors.toList())) {
            for (File downloadedFile : archiveDir.listFiles()) {
                String fileName = FilenameUtils.getBaseName(downloadedFile.getAbsolutePath());
                downloadedTracks.add(removeUnusedSymbols(fileName));
            }
        }
    }

    public boolean shouldDownload(Track track) {
        String bandName = track.getBand();
        String trackName = track.getTrack();
        return !exists(bandName.trim(), trackName.trim())
                && !bandName.contains("Программа ЖИВЫЕ");
    }

    private boolean exists(String bandName, String trackName) {
        String name = FilenameUtils.getBaseName(FileSaver.getName(bandName, trackName, ""));
        return downloadedTracks.contains(removeUnusedSymbols(name));
    }

    private String removeUnusedSymbols(String fileName) {
        return fileName.replaceAll("-", "").replaceAll(" ", "").toLowerCase();
    }
}
