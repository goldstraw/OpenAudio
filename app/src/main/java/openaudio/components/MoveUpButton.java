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

public class MoveUpButton extends Button {

    private Image moveUpImage = new Image(getClass().getResourceAsStream("/img/move-up.png"));
    private double width = Screen.getPrimary().getBounds().getWidth();

    public MoveUpButton(Song song, SongCollection songCollection) {
        super();
        ImageView moveUpView = new ImageView(moveUpImage);
        moveUpView.setFitHeight(width / 120);
        moveUpView.setFitWidth(width / 120);
        this.setGraphic(moveUpView);

        this.setOnAction(event -> {
            songCollection.moveUp(song);
            FocusView.getInstance().display();
        });
        this.getStyleClass().add("generic-button");
        this.getStyleClass().add("grey-bg");
    }
}