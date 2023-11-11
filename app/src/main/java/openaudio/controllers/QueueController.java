package openaudio.controllers;

import openaudio.models.Song;
import openaudio.models.SongCollection;
import openaudio.utils.Settings;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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

    public void addRecommendation() {
        // TODO: Implement
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
}