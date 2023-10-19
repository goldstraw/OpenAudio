package openaudio.models;

import java.io.File;
import openaudio.utils.Settings;
import javafx.scene.image.Image;
import java.util.List;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.io.PrintWriter;

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
        updatePlaylistInfoFile();
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
        updatePlaylistInfoFile();
    }

    public List<Song> loadSongs() {
        String musicFolder = Settings.getInstance().musicFolder;
        String playlistFolder = musicFolder + "/" + this.filePath;
        String playlistInfoPath = playlistFolder + "/" + this.title + ".playlistinfo";

        List<Song> songs = new ArrayList<Song>();

        try {
            Scanner scanner = new Scanner(new File(playlistInfoPath));
            // Iterate through lines in file
            while (scanner.hasNextLine()) {
                String songFilePath = scanner.nextLine();
                Song song = new Song(songFilePath);
                songs.add(song);
            }
        } catch (java.io.FileNotFoundException ex) {
            System.out.println("Could not find file: " + playlistInfoPath);
        }

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

    private void updatePlaylistInfoFile() {
        String musicFolder = Settings.getInstance().musicFolder;
        String playlistFolder = musicFolder + "/" + this.filePath;
        String playlistInfoPath = playlistFolder + "/" + this.title + ".playlistinfo";
        try {
            // Open and clear the file
            PrintWriter writer = new PrintWriter(playlistInfoPath, "UTF-8");
            for (Song song : this.songs) {
                writer.println(song.getFilePath());
            }
            writer.close();
        } catch (java.io.FileNotFoundException ex) {
            System.out.println("Could not find file: " + playlistInfoPath);
        } catch (java.io.UnsupportedEncodingException ex) {
            System.out.println("Unsupported encoding: UTF-8");
        }
    }

    public void moveUp(Song song) {
        int index = this.songs.indexOf(song);
        if (index > 0) {
            Collections.swap(this.songs, index, index - 1);
        }
        updatePlaylistInfoFile();
    }

    public void moveDown(Song song) {
        int index = this.songs.indexOf(song);
        if (index < this.songs.size() - 1) {
            Collections.swap(this.songs, index, index + 1);
        }
        updatePlaylistInfoFile();
    }
}