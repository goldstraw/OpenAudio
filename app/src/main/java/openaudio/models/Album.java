package openaudio.models;

import openaudio.utils.Settings;
import java.io.File;
import javafx.scene.image.Image;
import java.util.List;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;
import java.io.PrintWriter;

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
        updateAlbumInfoFile();

        // Load the cover image
        this.coverImage = loadCoverImage();
    }

    public List<Song> loadSongs() {
        String musicFolder = Settings.getInstance().musicFolder;
        String albumFolder = musicFolder + "/" + this.filePath;
        String albumInfoPath = albumFolder + "/" + this.filePath + ".albuminfo";

        List<Song> songs = new ArrayList<Song>();

        try {
            File albumInfo = new File(albumInfoPath);
            if (!albumInfo.exists()) {
                String[] allFiles = new File(albumFolder).list();
                for (int i = 0; i < allFiles.length; i++) {
                    if (allFiles[i].endsWith(".mp3") || allFiles[i].endsWith(".wav")) {
                        Song song = new Song(albumFolder + "/" + allFiles[i]);
                        songs.add(song);
                    }
                }

                // Sort songs by track attribute
                Collections.sort(songs, new Comparator<Song>() {
                    @Override
                    public int compare(Song song1, Song song2) {
                        return song1.getTrack().compareTo(song2.getTrack());
                    }
                });
            } else {
                java.util.Scanner scanner = new java.util.Scanner(albumInfo);
                // Iterate through lines in file
                while (scanner.hasNextLine()) {
                    String songFilePath = scanner.nextLine();
                    Song song = new Song(songFilePath);
                    songs.add(song);
                }
            }
        } catch (java.io.FileNotFoundException ex) {
            System.out.println("Could not find file: " + albumInfoPath);
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

    private void updateAlbumInfoFile() {
        String musicFolder = Settings.getInstance().musicFolder;
        String albumFolder = musicFolder + "/" + this.filePath;
        String albumInfoPath = albumFolder + "/" + this.filePath + ".albuminfo";
        try {
            // Open and clear the file
            PrintWriter writer = new PrintWriter(albumInfoPath, "UTF-8");
            for (Song song : this.songs) {
                writer.println(song.getFilePath());
            }
            writer.close();
        } catch (java.io.FileNotFoundException ex) {
            System.out.println("Could not find file: " + albumInfoPath);
        } catch (java.io.UnsupportedEncodingException ex) {
            System.out.println("Unsupported encoding: UTF-8");
        }
    }

    public void moveUp(Song song) {
        int index = this.songs.indexOf(song);
        if (index > 0) {
            Collections.swap(this.songs, index, index - 1);
        }
        updateAlbumInfoFile();
    }

    public void moveDown(Song song) {
        int index = this.songs.indexOf(song);
        if (index < this.songs.size() - 1) {
            Collections.swap(this.songs, index, index + 1);
        }
        updateAlbumInfoFile();
    }
}