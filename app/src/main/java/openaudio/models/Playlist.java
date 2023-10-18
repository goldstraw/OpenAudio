package openaudio.models;

import java.io.File;
import openaudio.utils.Settings;
import javafx.scene.image.Image;
import java.util.List;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;

public class Playlist implements SongCollection {

    private String title;
    private String artist;
    private String filePath;
    private List<Song> songs;
    private Image coverImage;

    public Playlist(String filePath) {
        this.filePath = filePath;
        this.title = filePath;
        this.artist = "User";

        // Create playlist folder if it doesn't exist
        String musicFolder = Settings.getInstance().musicFolder;
        String playlistFolder = musicFolder + "/" + this.filePath;
        File file = new File(playlistFolder);
        if (!file.exists()) {
            file.mkdir();
        }

        this.songs = loadSongs();
        this.coverImage = loadCoverImage();
    }

    public void addSong(Song song) {
        this.songs.add(song);

        // Write song file path to .playlistentry file
        String musicFolder = Settings.getInstance().musicFolder;
        String playlistFolder = musicFolder + "/" + this.filePath;
        String playlistEntryFilePath = playlistFolder + "/" + song.getTitle() + ".playlistentry";
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter(playlistEntryFilePath, "UTF-8");
            writer.println(song.getFilePath());
            writer.println(this.songs.size() - 1);
            writer.close();
        } catch (java.io.FileNotFoundException ex) {
            System.out.println("Could not find file: " + playlistEntryFilePath);
        } catch (java.io.UnsupportedEncodingException ex) {
            System.out.println("Unsupported encoding: UTF-8");
        }
    }

    public List<Song> loadSongs() {
        String musicFolder = Settings.getInstance().musicFolder;
        String playlistFolder = musicFolder + "/" + this.filePath;
        String[] allFiles = new File(playlistFolder).list();

        List<Song> songs = new ArrayList<Song>();

        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].endsWith(".playlistentry")) {
                // Read song file path from .playlistentry file
                String songFilePath = "";
                int songOrder = 0;
                try {
                    java.util.Scanner scanner = new java.util.Scanner(new File(playlistFolder + "/" + allFiles[i]));
                    songFilePath = scanner.nextLine();
                    songOrder = Integer.parseInt(scanner.nextLine());
                } catch (java.io.FileNotFoundException ex) {
                    System.out.println("Could not find file: " + playlistFolder + "/" + allFiles[i]);
                }
                songs.add(new Song(songFilePath, songOrder));
            }
        }

        Collections.sort(songs, new Comparator<Song>() {
            public int compare(Song s1, Song s2) {
                return s1.getOrder() - s2.getOrder();
            }
        });

        return songs;
    }

    public Image loadCoverImage() {
        String musicFolder = Settings.getInstance().musicFolder;
        String playlistFolder = musicFolder + "/" + this.filePath;
        String[] allFiles = new File(playlistFolder).list();

        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].endsWith(".jpg") || allFiles[i].endsWith(".png")) {
                try {
                    File file = new File(playlistFolder + "/" + allFiles[i]);
                    String imageURL = file.toURI().toURL().toString();
                    return new Image(imageURL);
                } catch (MalformedURLException ex) {
                    System.out.println("Invalid URL for image");
                }
            
            }
        }

        return null;
    }

    public List<Song> getRemainingSongs(Song currentSong) {
        List<Song> remainingSongs = new ArrayList<Song>();
        boolean foundCurrentSong = false;
        for (Song song : this.songs) {
            if (song == currentSong) {
                foundCurrentSong = true;
            } else if (foundCurrentSong) {
                remainingSongs.add(song);
            }
        }
        return remainingSongs;
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