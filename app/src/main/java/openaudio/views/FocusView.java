package openaudio.views;

import openaudio.models.SongCollection;

public class FocusView {
    private SongCollection songCollection;
    
    public FocusView(SongCollection songCollection) {
        this.songCollection = songCollection;
        // and then render the songs with songCollection.getSongs().
    }

    public void setSongCollection(SongCollection songCollection) {
        this.songCollection = songCollection;
    }

    public SongCollection getSongCollection() {
        return this.songCollection;
    }

    public void render() {
        // render the songs with songCollection.getSongs().
    }
}