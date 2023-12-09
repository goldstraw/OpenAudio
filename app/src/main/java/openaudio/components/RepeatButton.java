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

public class RepeatButton extends Button {
    private boolean repeating = Settings.getInstance().getRepeat();
    private Image repeatImage = new Image(getClass().getResourceAsStream("/img/repeat.png"));
    private double width = Screen.getPrimary().getBounds().getWidth();

    public RepeatButton() {
        super();
        ImageView repeatView = new ImageView(repeatImage);
        repeatView.setFitHeight(width / 80);
        repeatView.setFitWidth(width / 80);
        this.setGraphic(repeatView);
        this.getStyleClass().add("grey-bg");

        if (this.repeating) {
            this.getStyleClass().add("green-bg");
            this.getStyleClass().remove("grey-bg");
        }

        this.setOnAction(event -> {
            this.repeating = !this.repeating;
            this.getStyleClass().clear();
            Settings.getInstance().setRepeat(this.repeating);
            this.getStyleClass().add("button");
            this.getStyleClass().add("grey-bg");
            if (this.repeating) {
                this.getStyleClass().add("green-bg");
                this.getStyleClass().remove("grey-bg");
            }
        });
    }
}