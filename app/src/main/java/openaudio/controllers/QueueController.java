package openaudio.controllers;

import openaudio.models.Song;
import openaudio.models.SongCollection;
import java.util.LinkedList;
import java.util.List;

// Singleton
public class QueueController {

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

    public void setCollection(SongCollection collection) {
        this.collectionQueue.clear();
        for (Song song : collection.getSongs()) {
            this.collectionQueue.add(song);
        }
    }

    public void setCollection(List<Song> collection) {
        this.collectionQueue.clear();
        for (Song song : collection) {
            this.collectionQueue.add(song);
        }
    }

    public void addRecommendation() {
        // TODO: Implement
    }

    public Song getNextSong() {
        if (this.userQueue.size() > 0) {
            return this.userQueue.removeFirst();
        } else if (this.collectionQueue.size() > 0) {
            return this.collectionQueue.removeFirst();
        } else {
            addRecommendation();
            return this.recommendationQueue.removeFirst();
        }
    }
}