package openaudio.components;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.controllers.MusicPlayerController;
import openaudio.controllers.QueueController;
import openaudio.views.FocusView;
import openaudio.utils.Settings;
import javafx.stage.Screen;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class SongButton extends Button {

    public SongButton(Song song, SongCollection songCollection) {
        super(song.getTitle());
        this.setOnAction(event -> {
            MusicPlayerController.getInstance().playSong(song);
            // If shuffle isn't on, only add the remaining songs to the queue
            if (!Settings.getInstance().getShuffle()) {
                List<Song> remainingSongs = songCollection.getRemainingSongs(song);
                QueueController.getInstance().setCollection(remainingSongs);
            } else {
                List<Song> shuffleSongs = songCollection.getSongs();
                // Remove the song that was just played
                shuffleSongs.remove(song);
                QueueController.getInstance().setCollection(shuffleSongs);
            }
            QueueController.getInstance().clearFutureQueue();
        });
        this.getStyleClass().add("song-button");
    }
}