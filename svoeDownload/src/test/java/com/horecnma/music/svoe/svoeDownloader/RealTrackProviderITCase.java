package com.horecnma.music.svoe.svoeDownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.horecnma.music.svoe.svoeDownloader.dto.Track;

import net.sf.ehcache.util.NamedThreadFactory;

/**
 * @author Mikhail
 */
@ContextConfiguration(classes = {TestConfig.class})
public class RealTrackProviderITCase extends AbstractJUnit4SpringContextTests {
    private static final Logger LOG = LoggerFactory.getLogger(RealTrackProviderITCase.class);

    @Autowired
    private BandListTrackProvider trackProvider;
    private ExecutorService executor = Executors.newFixedThreadPool(4, new NamedThreadFactory("test-"));

    @Test
    public void shouldName()
            throws Exception {
        TrackQueue tracks = trackProvider.startGetTracks();
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
