package com.horecnma.music.svoe.svoeDownloader;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.horecnma.music.svoe.svoeDownloader.dto.Track;

/**
 * @author mnikolaev
 */
@Service
public class AllTracksDownloader {
    private static final Logger LOG = Logger.getLogger(AllTracksDownloader.class);

    @Autowired
    private SingleTrackDownloader trackLoader;
    @Autowired
    private FileDownoloadDecision fileDownoloadDecision;

    private final ExecutorService exec = Executors.newFixedThreadPool(10);
    private final List<Wrong> exceptions = new Vector<>();
    private int downloadCounter = 0;

    @Autowired
    private BandListTrackProvider bandListTrackProvider;

    public synchronized void downloadAllTracks() {
        TrackQueue tracks = bandListTrackProvider.startGetTracks();
        exec.execute(new DownloadTracksTask(tracks));
        downloadCounter = 0;
        System.out.println("Завершение");
        exec.shutdown();
    }

    private void doSynchDownload(Track track) {
        String bandName = track.getBand();
        String trackName = track.getTrack();
        System.out.println(String.format("%s: %s - %s", Thread.currentThread().getName(), bandName, trackName));

        int currentDownloadNumber = downloadCounter++;
        try {
            trackLoader.doLoadFile(bandName, trackName);
            System.out.println(currentDownloadNumber + " ок  " + bandName + " - " + trackName);
        } catch (Exception e) {
            trackLoader.delete(bandName, trackName);
            try {
                trackLoader.doLoadFile(bandName, " " + trackName);
                System.out.println(currentDownloadNumber + " ок  " + bandName + " - " + trackName);
            } catch (Exception e1) {
                trackLoader.delete(bandName, trackName);
                System.err.println(currentDownloadNumber + "    " + bandName + " - " + trackName + "\t\t\t" + e.getMessage());
                exceptions.add(new Wrong(e, bandName, trackName));
            }
        }
    }

    private static class Wrong {
        Exception e;
        String band;
        String track;

        private Wrong(Exception e, String band, String tracEventListenerk) {
            this.e = e;
            this.band = band;
            this.track = track;
        }

        public Exception getE() {
            return e;
        }

        public String getBand() {
            return band;
        }

        public String getTrack() {
            return track;
        }
    }

    private class DownloadTracksTask implements Runnable {
        private final TrackQueue tracks;

        public DownloadTracksTask(TrackQueue tracks) {this.tracks = tracks;}

        @Override
        public void run() {
            while (!tracks.isCommitted()) {
                Track nextTrack = tracks.getNextTrack();
                if (nextTrack == null) {
                    waitForNextTrack(500);
                } else if (fileDownoloadDecision.shouldDownload(nextTrack)) {
                    AllTracksDownloader.this.doSynchDownload(nextTrack);
                }
            }
            LOG.debug("task finished");
        }

        private void waitForNextTrack(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
