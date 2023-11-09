package openaudio.components;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.controllers.MusicPlayerController;
import openaudio.controllers.QueueController;
import openaudio.views.FocusView;
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
            List<Song> remainingSongs = songCollection.getRemainingSongs(song);
            QueueController.getInstance().setCollection(remainingSongs);
            QueueController.getInstance().clearFutureQueue();
        });
        this.getStyleClass().add("song-button");
    }
}