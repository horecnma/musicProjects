package svoeDownloader;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author mnikolaev
 */
public class AllTracksDownloader {
    private static final Logger log = Logger.getLogger(AllTracksDownloader.class);

    private final SingleTrackDownloader trackLoader;
    private final ExecutorService exec = Executors.newFixedThreadPool(10);
    private final List<Wrong> exceptions = new Vector<>();
    private final FileDownoloadDecision fileDownoloadDecision;
    private int downloadCounter = 0;

    public AllTracksDownloader(String destination, String[] archives) {
        trackLoader = new SingleTrackDownloader(destination);
        fileDownoloadDecision = new FileDownoloadDecision(archives);
    }

    public synchronized void downloadAllTracks()
            throws IOException {
        downloadCounter = 0;
        for (int i = 0; i < 10; i++) {
            Document doc = Jsoup.connect("https://svoeradio.fm/air/artists/?paged=" + (i + 1)).get();
            Elements bandLinks = doc.select("li.artist-list-point a");
            for (Element bandLink : bandLinks) {
                try {
                    Document bandDoc = Jsoup.connect(bandLink.attr("href")).get();
                    Elements tracks = bandDoc.select("div.track-list-wrapper div.track-list-artist-track");
                    for (Element track : tracks) {
                        String bandName = track.select("div.track-list-artist").text();
                        String trackName = track.select("div.track-list-track").text();

                        if (fileDownoloadDecision.shouldDownload(bandName, trackName)) {
                            doAsyncDownload(bandName, trackName);
                        } else {

                        }
                    }
                } catch (SocketTimeoutException e) {
                    log.error(e, e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Завершение");
        exec.shutdown();
    }

    public void doAsyncDownload(String bandName, String trackName) {
        exec.execute(() -> {
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
        });
    }

    private static class Wrong {
        Exception e;
        String band;
        String track;

        private Wrong(Exception e, String band, String track) {
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
}
