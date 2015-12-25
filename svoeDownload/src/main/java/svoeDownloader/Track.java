package svoeDownloader;

import org.apache.log4j.Logger;

/**
 * @author mnikolaev
 */
public class Track {
    private String trackName;
    private String bandName;

    public Track(String trackName, String bandName) {
        this.trackName = trackName;
        this.bandName = bandName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }
}
