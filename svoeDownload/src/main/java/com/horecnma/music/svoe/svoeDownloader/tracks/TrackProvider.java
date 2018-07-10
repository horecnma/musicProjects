package com.horecnma.music.svoe.svoeDownloader.tracks;

import com.horecnma.music.svoe.svoeDownloader.TrackQueue;

/**
 * @author Mikhail
 */
public interface TrackProvider {
    TrackQueue startGetTracks();
}
