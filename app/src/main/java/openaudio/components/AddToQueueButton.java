package openaudio.components;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.views.FocusView;
import openaudio.controllers.QueueController;
import javafx.stage.Screen;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddToQueueButton extends Button {

    private Image addToQueueImage = new Image(getClass().getResourceAsStream("/img/add-to-queue.png"));
    private double width = Screen.getPrimary().getBounds().getWidth();

    public AddToQueueButton(Song song) {
        super();
        ImageView addToQueueView = new ImageView(addToQueueImage);
        addToQueueView.setFitHeight(width / 120);
        addToQueueView.setFitWidth(width / 120);
        this.setGraphic(addToQueueView);

        this.setOnAction(event -> {
            QueueController.getInstance().addUserSong(song);
        });
        this.getStyleClass().add("generic-button");
        this.getStyleClass().add("grey-bg");
    }
}