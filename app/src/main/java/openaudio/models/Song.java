package openaudio.models;

import java.io.File;

public class Song {

    private String title;
    private String artist;
    private String album;
    private float duration; // in seconds
    private String filePath;

    public Song(String title, String artist, String album, float duration, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.filePath = new File(filePath).toURI().toString();
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFilePath() {
        return this.filePath;
    }
}