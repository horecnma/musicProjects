package com.horecnma.music.svoe.svoeDownloader;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.horecnma.music.svoe.svoeDownloader.dto.BandLink;
import com.horecnma.music.svoe.svoeDownloader.dto.Track;

/**
 * @author Mikhail
 */
@Service
public class BandListTrackProvider {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BandListTrackProvider.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ExecutorService trackProviderExecutor;
    @Autowired
    private SvoeConnector svoeConnector;

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
            getTracksFromPage(result, pageIndex);
        }
        result.commit();
    }

    private void getTracksFromPage(TrackQueue result, int pageIndex) {
        try {
            LOG.debug("Collect tracks from page " + (pageIndex + 1));
            for (BandLink bandLink : svoeConnector.getBandLinksFromPage(pageIndex)) {
                try {
                    List<Track> bandTracks = svoeConnector.getBandTracks(bandLink);
                    result.addTracks(bandTracks);
                    LOG.debug("count: " + result.totalSize() + ", current: " + result.currentSize());
                } catch (SocketTimeoutException e) {
                    LOG.error("error", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            LOG.error(e, e);
        }
    }
}
