package com.horecnma.music.svoe.svoeDownloader.tracks;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.horecnma.music.svoe.svoeDownloader.SvoeConnector;
import com.horecnma.music.svoe.svoeDownloader.TrackQueue;
import com.horecnma.music.svoe.svoeDownloader.dto.Track;

/**
 * @author Mikhail
 */
@Service
public class PremiereTrackProvider implements TrackProvider {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PremiereTrackProvider.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ExecutorService trackProviderExecutor;
    @Autowired
    private SvoeConnector svoeConnector;

    @Value("${svoe.page.premier.max.page:27}")
    private int maxPremierePage;

    @Override
    public TrackQueue startGetTracks() {
        TrackQueue result = applicationContext.getBean(TrackQueue.class);
        trackProviderExecutor.execute(() -> getTracks(result));
        return result;
    }

    private void getTracks(TrackQueue result) {
        try {
            for (int pageIndex = 0; pageIndex < maxPremierePage; pageIndex++) {
                if (Thread.interrupted()) {
                    LOG.debug(String.format("interrupted before processing page #%s", pageIndex));
                    return;
                }
                result.addTracks(getTracksFromPage(pageIndex, true));
                LOG.debug("count: " + result.totalSize() + ", current: " + result.currentSize());
            }
        } finally {
            result.commit();
        }
    }

    private List<Track> getTracksFromPage(int pageIndex, boolean allowRerun) {
        LOG.debug("Collect tracks from page " + (pageIndex + 1));
        try {
            List<Track> premieresFromPage = svoeConnector.getPremieresFromPage(pageIndex);
            LOG.debug("  Found " + premieresFromPage.size() + " tracks");
            return premieresFromPage;
        } catch (SocketTimeoutException e) {
            if (allowRerun) {
                sleep(2000);
                return getTracksFromPage(pageIndex, false);
            } else {
                LOG.error("error", e);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sleep(int duration) {
        try {
            LOG.debug("wait.... " + duration);
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
