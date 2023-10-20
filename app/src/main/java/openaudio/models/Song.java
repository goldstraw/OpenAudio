package openaudio.models;

import java.io.File;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Song {

    private String title;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private String track;
    private float duration; // in seconds
    private String filePath;

    public Song(String filePath) {
        this.filePath = filePath;
        // Read end of file path to get title
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        this.title = fileName.substring(0, fileName.lastIndexOf('.'));

        this.artist = "Unknown";
        this.album = "Unknown";
        this.year = "1970";
        this.genre = "Unknown";
        this.track = "";
        this.duration = 0;

        try {
            Mp3File mp3file = new Mp3File(filePath);
            this.duration = mp3file.getLengthInSeconds();
            if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                this.title = id3v1Tag.getTitle();
                this.artist = id3v1Tag.getArtist();
                this.album = id3v1Tag.getAlbum();
                this.year = id3v1Tag.getYear();
                this.genre = id3v1Tag.getGenreDescription();
                this.track = id3v1Tag.getTrack();
            } else if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                this.title = id3v2Tag.getTitle();
                this.artist = id3v2Tag.getArtist();
                this.album = id3v2Tag.getAlbum();
                this.year = id3v2Tag.getYear();
                this.genre = id3v2Tag.getGenreDescription();
                this.track = id3v2Tag.getTrack();
            }
        } catch (Exception e) {
            System.out.println("Error reading mp3 file");
        }

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
        return this.title;
    }

    public float getDuration() {
        return this.duration;
    }

    public String getYear() {
        return this.year;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getTrack() {
        return this.track;
    }
}