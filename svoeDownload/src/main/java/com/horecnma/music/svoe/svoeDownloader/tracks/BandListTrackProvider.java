package com.horecnma.music.svoe.svoeDownloader.tracks;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.horecnma.music.svoe.svoeDownloader.SvoeConnector;
import com.horecnma.music.svoe.svoeDownloader.TrackQueue;
import com.horecnma.music.svoe.svoeDownloader.dto.BandLink;
import com.horecnma.music.svoe.svoeDownloader.dto.Track;

/**
 * @author Mikhail
 */
@Service
public class BandListTrackProvider implements TrackProvider {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BandListTrackProvider.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ExecutorService trackProviderExecutor;
    @Autowired
    private SvoeConnector svoeConnector;

    @Override
    public TrackQueue startGetTracks() {
        TrackQueue result = applicationContext.getBean(TrackQueue.class);
        trackProviderExecutor.execute(() -> getTracks(result));
        return result;
    }

    private void getTracks(TrackQueue result) {
        for (int pageIndex = 0; pageIndex < 10; pageIndex++) {
            if (Thread.interrupted()) {
                LOG.debug(String.format("interrupted before processing page #%s", pageIndex));
                result.commit();
                return;
            }
            result.addTracks(getTracksFromPage(pageIndex));
            LOG.debug("count: " + result.totalSize() + ", current: " + result.currentSize());
        }
        result.commit();
    }

    private List<Track> getTracksFromPage(int pageIndex) {
        try {
            LOG.debug("Collect tracks from page " + (pageIndex + 1));
            List<Track> pageTracks = new ArrayList<>();
            for (BandLink bandLink : svoeConnector.getBandLinksFromPage(pageIndex)) {
                try {
                    List<Track> bandTracks = svoeConnector.getBandTracks(bandLink);
                    pageTracks.addAll(bandTracks);
                } catch (SocketTimeoutException e) {
                    LOG.error("error", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            LOG.debug("  Found " + pageTracks.size() + " tracks");
            return pageTracks;
        } catch (IOException e) {
            LOG.error(e, e);
            return new ArrayList<>();
        }
    }
}
