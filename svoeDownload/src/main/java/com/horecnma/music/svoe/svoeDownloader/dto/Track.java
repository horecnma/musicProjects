package com.horecnma.music.svoe.svoeDownloader.dto;

/**
 * @author Mikhail
 */
public class Track {
    private String band;
    private String track;

    public Track(String band, String track) {
        this.band = band;
        this.track = track;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
