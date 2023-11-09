package openaudio.components;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.models.Playlist;
import openaudio.controllers.MusicPlayerController;
import openaudio.controllers.QueueController;
import openaudio.views.FocusView;
import javafx.stage.Screen;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class RemoveFromPlaylistButton extends Button {

    private Image removeImage = new Image(getClass().getResourceAsStream("/img/remove-icon.png"));
    private double width = Screen.getPrimary().getBounds().getWidth();

    public RemoveFromPlaylistButton(Song song, SongCollection songCollection) {
        super();
        ImageView removeView = new ImageView(removeImage);
        removeView.setFitHeight(width / 120);
        removeView.setFitWidth(width / 120);
        this.setGraphic(removeView);
        this.setOnAction(event -> {
            Playlist playlist = (Playlist) songCollection;
            playlist.removeSong(song);
            FocusView.getInstance().display();
        });
        this.getStyleClass().add("generic-button");
    }
}