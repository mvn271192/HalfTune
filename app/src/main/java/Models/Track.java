package Models;

/**
 * Created by manikandanvnair on 15/05/18.
 */

public class Track {

    String name;
    String artist;
    String previewUrl;
    String artUrl;
    boolean isDownloaded;
    String trackId;

    public Track(String name, String artist, String previewUrl, String artUrl, String trackId) {
        this.name = name;
        this.artist = artist;
        this.previewUrl = previewUrl;
        this.artUrl = artUrl;
        this.isDownloaded = false;
        this.trackId = trackId;

    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getArtUrl() {
        return artUrl;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public String getTrackId() {
        return trackId;
    }
}
