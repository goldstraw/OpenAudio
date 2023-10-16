package openaudio.models;

import java.io.File;
import openaudio.utils.Settings;

public class Album {

    private String title;
    private String artist;
    private String filePath;
    private Song[] songs;

    public Album(String filePath) {
        this.filePath = filePath;
        this.title = filePath;
        this.artist = "Unknown";
        this.songs = loadSongs();
    }

    public Song[] loadSongs() {
        String musicFolder = Settings.getInstance().musicFolder;
        String[] songFiles = new File(musicFolder + "/" + this.filePath).list();

        Song[] songs = new Song[songFiles.length];

        for (int i = 0; i < songFiles.length; i++) {
            songs[i] = new Song(songFiles[i]);
        }

        return songs;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getName() {
        return this.title;
    }

    public Song[] getSongs() {
        return this.songs;
    }
}