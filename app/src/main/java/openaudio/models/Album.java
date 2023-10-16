package openaudio.models;

import java.io.File;
import openaudio.utils.Settings;
import javafx.scene.image.Image;
import java.util.List;
import java.util.ArrayList;
import java.net.MalformedURLException;

public class Album implements SongCollection {

    private String title;
    private String artist;
    private String filePath;
    private List<Song> songs;
    private Image coverImage;

    public Album(String filePath) {
        this.filePath = filePath;
        this.title = filePath;
        this.artist = "Unknown";
        this.songs = loadSongs();

        // Load the cover image
        this.coverImage = loadCoverImage();
    }

    public List<Song> loadSongs() {
        String musicFolder = Settings.getInstance().musicFolder;
        String albumFolder = musicFolder + "/" + this.filePath;
        String[] allFiles = new File(albumFolder).list();

        List<Song> songs = new ArrayList<Song>();

        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].endsWith(".mp3") || allFiles[i].endsWith(".wav")) {
                songs.add(new Song(albumFolder + "/" + allFiles[i]));
            }
        }

        return songs;
    }

    public Image loadCoverImage() {
        String musicFolder = Settings.getInstance().musicFolder;
        String albumFolder = musicFolder + "/" + this.filePath;
        String[] allFiles = new File(albumFolder).list();

        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].endsWith(".jpg") || allFiles[i].endsWith(".png")) {
                try {
                    File file = new File(albumFolder + "/" + allFiles[i]);
                    String imageURL = file.toURI().toURL().toString();
                    return new Image(imageURL);
                } catch (MalformedURLException ex) {
                    System.out.println("Invalid URL for image");
                }
            
            }
        }

        return null;
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

    public List<Song> getSongs() {
        return this.songs;
    }

    public Image getCoverImage() {
        return this.coverImage;
    }
}