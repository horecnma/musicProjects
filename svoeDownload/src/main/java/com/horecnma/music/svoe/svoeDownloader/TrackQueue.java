package com.horecnma.music.svoe.svoeDownloader;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.cxf.jaxrs.ext.Nullable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.horecnma.music.svoe.svoeDownloader.dto.Track;

/**
 * @author Mikhail
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrackQueue {

    private final Queue<Track> tracks = new LinkedList<>();
    private int totalCount=0;
    private boolean committed = false;

    public synchronized void addTrack(Track track) {
        tracks.add(track);
        totalCount++;
    }

    public synchronized void addTracks(Collection<Track> tracks) {
        this.tracks.addAll(tracks);
        totalCount+=tracks.size();
    }

    @Nullable
    public synchronized Track getNextTrack() {
        return tracks.poll();
    }

    public synchronized boolean isCommitted(){
        return committed;
    }

    public synchronized void commit() {
        committed = true;
    }

    public int totalSize() {
        return totalCount;
    }

    public int currentSize() {
        return tracks.size();
    }
}
