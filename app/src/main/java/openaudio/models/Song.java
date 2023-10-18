package openaudio.models;

import java.io.File;

public class Song {

    private String title;
    private String artist;
    private String album;
    private float duration; // in seconds
    private String filePath;
    private int order;

    public Song(String filePath, int order) {
        this.filePath = filePath;
        // Read end of file path to get title
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        this.title = fileName.substring(0, fileName.lastIndexOf('.'));

        this.artist = "Unknown";
        this.album = "Unknown";
        this.duration = 0;
        this.order = order;
    }

    public Song(String title, String artist, String album, float duration, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.filePath = filePath;
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

    public String getTitle() {
        return title;
    }

    public float getDuration() {
        return duration;
    }

    public int getOrder() {
        return order;
    }
}