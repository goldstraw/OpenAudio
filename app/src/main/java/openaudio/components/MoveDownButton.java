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

public class MoveDownButton extends Button {

    private Image moveDownImage = new Image(getClass().getResourceAsStream("/img/move-down.png"));
    private double width = Screen.getPrimary().getBounds().getWidth();

    public MoveDownButton(Song song, SongCollection songCollection) {
        super();
        ImageView moveDownView = new ImageView(moveDownImage);
        moveDownView.setFitHeight(width / 120);
        moveDownView.setFitWidth(width / 120);
        this.setGraphic(moveDownView);

        this.setOnAction(event -> {
            songCollection.moveDown(song);
            FocusView.getInstance().display();
        });
        this.getStyleClass().add("generic-button");
        this.getStyleClass().add("grey-bg");
    }
}