package openaudio.controllers;

import openaudio.models.Song;
import openaudio.models.Album;
import openaudio.models.SongCollection;
import openaudio.controllers.CollectionController;
import openaudio.utils.Settings;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

// Singleton
public class QueueController {

    private LinkedList<Song> history = new LinkedList<Song>();
    private LinkedList<Song> futureQueue = new LinkedList<Song>();
    private LinkedList<Song> userQueue = new LinkedList<Song>();
    private LinkedList<Song> collectionQueue = new LinkedList<Song>();
    private LinkedList<Song> recommendationQueue = new LinkedList<Song>();

    private static QueueController instance = null;

    public static QueueController getInstance() {
        if (instance == null) {
            instance = new QueueController();
        }
        return instance;
    }

    private QueueController() {
    }

    public void addUserSong(Song song) {
        this.userQueue.add(song);
    }

    // Add song to the front of the queue, so it plays next
    // Used when the user goes back to a previous song
    public void addUserSongToFront(Song song) {
        this.userQueue.addFirst(song);
    }

    // Shuffle collection queue
    public void shuffleCollection() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < this.collectionQueue.size(); i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for (int i = 0; i < numbers.size(); i++) {
            this.collectionQueue.add(this.collectionQueue.get(numbers.get(i)));
        }

        for (int i = 0; i < numbers.size(); i++) {
            this.collectionQueue.removeFirst();
        }
    }

    public void setCollection(List<Song> collection) {
        this.collectionQueue.clear();
        boolean shuffle = Settings.getInstance().getShuffle();
        if (shuffle) {
            List<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < collection.size(); i++) {
                numbers.add(i);
            }
            Collections.shuffle(numbers);
            
            for (int i = 0; i < collection.size(); i++) {
                this.collectionQueue.add(collection.get(numbers.get(i)));
            }
        } else {
            for (Song song : collection) {
                this.collectionQueue.add(song);
            }
        }
    }

    // Sort collection queue by track number
    public void sortCollection() {
        Collections.sort(this.collectionQueue, new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                String s1 = song1.getTrack();
                String s2 = song2.getTrack();

                if(s1.length() < s2.length()){
                    return s2.length() - s1.length();
                }
                else if(s1.length() > s2.length()){
                    return s1.length() - s2.length();
                }
                for (int i = 0; i < s1.length() && i < s2.length(); i++) {
                    if (s1.charAt(i) == s2.charAt(i)) {
                        //System.out.println("Equal");
                        continue;
                    } else {
                        return s1.charAt(i) - s2.charAt(i);
                    }
                }
                return 0;
            }
        });
    }

    public void addRecommendation() {
        // Randomly select any song from the user's library
        // Re-select if the song is in the history
        List<Album> albums = CollectionController.getInstance().getAlbums();

        while (this.recommendationQueue.size() < 5) {
            Random random = new Random();
            Album randomAlbum = albums.get(random.nextInt(albums.size()));
            List<Song> songs = randomAlbum.getSongs();
            Song randomSong = songs.get(random.nextInt(songs.size()));
            if (!this.history.contains(randomSong)) {
                this.recommendationQueue.add(randomSong);
            }
        }
    }

    public Song getNextSong() {
        if (this.futureQueue.size() > 0) {
            return this.futureQueue.removeFirst();
        } else if (this.userQueue.size() > 0) {
            return this.userQueue.removeFirst();
        } else if (this.collectionQueue.size() > 0) {
            return this.collectionQueue.removeFirst();
        } else {
            addRecommendation();
            return this.recommendationQueue.removeFirst();
        }
    }

    public Song getPreviousSong() {
        if (this.history.size() > 0) {
            return this.history.removeLast();
        } else {
            return null;
        }
    }

    public void addSongToHistory(Song song) {
        this.history.add(song);
    }

    public void addFutureSong(Song song) {
        this.futureQueue.addFirst(song);
    }

    public void clearFutureQueue() {
        this.futureQueue.clear();
    }

    public void clearCollectionQueue() {
        this.collectionQueue.clear();
    }
}