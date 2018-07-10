package com.horecnma.music.svoe.svoeDownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.horecnma.music.svoe.svoeDownloader.dto.Track;
import com.horecnma.music.svoe.svoeDownloader.tracks.BandListTrackProvider;
import com.horecnma.music.svoe.svoeDownloader.tracks.PremiereTrackProvider;
import com.horecnma.music.svoe.svoeDownloader.tracks.TrackProvider;

import net.sf.ehcache.util.NamedThreadFactory;

/**
 * @author Mikhail
 */
@ContextConfiguration(classes = {TestConfig.class})
public class RealTrackProviderITCase extends AbstractJUnit4SpringContextTests {

    @Autowired
    private BandListTrackProvider bandListTrackProvider;
    @Autowired
    private PremiereTrackProvider premiereTrackProvider;

    private ExecutorService executor = Executors.newFixedThreadPool(4, new NamedThreadFactory("test-"));

    @Test
    public void testBandListTrackProvider()
            throws Exception {
        runProvider(bandListTrackProvider);
    }

    @Test
    public void testPremiereTrackProvider()
            throws Exception {
        runProvider(premiereTrackProvider);
    }

    private void runProvider(TrackProvider trackQueue)
            throws InterruptedException {
        TrackQueue tracks = trackQueue.startGetTracks();
        executor.execute(() -> {
            while (!tracks.isCommitted()) {
                Track nextTrack = tracks.getNextTrack();
                if (nextTrack == null) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                } else {
                    System.out.println(String.format("%s: %s - %s", Thread.currentThread().getName(), nextTrack.getBand(), nextTrack.getTrack()));
                }
            }
            System.out.println("finish");
        });
        executor.shutdown();
        executor.awaitTermination(1000, TimeUnit.SECONDS);
    }
}
