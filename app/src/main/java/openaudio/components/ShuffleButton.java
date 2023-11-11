package openaudio.components;

import openaudio.models.SongCollection;
import openaudio.controllers.QueueController;
import openaudio.controllers.MusicPlayerController;
import openaudio.models.Song;
import openaudio.views.FocusView;
import openaudio.utils.Settings;
import openaudio.models.SongCollection;
import javafx.stage.Screen;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class ShuffleButton extends Button {
    private boolean shuffling = Settings.getInstance().getShuffle();
    private Image shuffleImage = new Image(getClass().getResourceAsStream("/img/shuffle.png"));
    private double width = Screen.getPrimary().getBounds().getWidth();

    public ShuffleButton() {
        super();
        ImageView shuffleView = new ImageView(shuffleImage);
        shuffleView.setFitHeight(width / 80);
        shuffleView.setFitWidth(width / 80);
        this.setGraphic(shuffleView);

        if (this.shuffling) {
            this.getStyleClass().add("green-bg");
        }

        this.setOnAction(event -> {
            this.shuffling = !this.shuffling;
            this.getStyleClass().clear();
            Settings.getInstance().setShuffle(this.shuffling);
            this.getStyleClass().add("button");
            if (this.shuffling) {
                this.getStyleClass().add("green-bg");
            }

            // Shuffle the remainder of the collection
            Song currentSong = MusicPlayerController.getInstance().getSong();
            QueueController.getInstance().shuffleCollection();
            QueueController.getInstance().clearFutureQueue();
        });
    }
}