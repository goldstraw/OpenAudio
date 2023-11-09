package openaudio.components;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.views.FocusView;
import javafx.stage.Screen;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddToPlaylistButton extends Button {

    private Song song;
    private boolean clicked = false;
    private Image addPlaylistImage = new Image(getClass().getResourceAsStream("/img/add-to-playlist.png"));
    private double width = Screen.getPrimary().getBounds().getWidth();

    public AddToPlaylistButton(Song song) {
        super();
        this.song = song;
        ImageView addPlaylistView = new ImageView(addPlaylistImage);
        addPlaylistView.setFitHeight(width / 120);
        addPlaylistView.setFitWidth(width / 120);
        this.setGraphic(addPlaylistView);
        this.getStyleClass().add("generic-button");
        this.getStyleClass().add("grey-bg");

        this.setOnAction(event -> {
            this.clicked = !this.clicked;
            this.getStyleClass().clear();
            if (this.clicked) {
                FocusView.getInstance().addPlaylistContender(this.song);
                this.getStyleClass().add("green-button");
                this.getStyleClass().add("button");
            } else {
                FocusView.getInstance().removePlaylistContender(this.song);
                this.getStyleClass().add("button");
                this.getStyleClass().add("generic-button");
                this.getStyleClass().add("grey-bg");
            }
        });
    }
}