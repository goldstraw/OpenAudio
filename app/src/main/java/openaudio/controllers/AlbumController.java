package openaudio.controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import openaudio.utils.Settings;
import openaudio.models.Album;
import javafx.scene.control.Label;
import java.io.File;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.TextAlignment;
import openaudio.views.FocusView;


public class AlbumController {

    private VBox vBox;
    private Button chooseDirButton;

    private Album[] loadAlbums() {
        String musicFolder = Settings.getInstance().musicFolder;
        String[] albumFolders = new File(musicFolder).list();

        Album[] albums = new Album[albumFolders.length];

        for (int i = 0; i < albumFolders.length; i++) {
            albums[i] = new Album(albumFolders[i]);
        }

        return albums;

    }

    private void initializeComponents(Stage primaryStage) {
        this.vBox = new VBox();
        this.vBox.setAlignment(Pos.CENTER);

        if (Settings.getInstance().musicFolder == null) {
            this.chooseDirButton = new Button("Choose Music Directory");
            vBox.getChildren().add(this.chooseDirButton);

            this.chooseDirButton.setOnAction(event -> {
                Settings.getInstance().chooseDirectory(primaryStage, this.chooseDirButton);
            });
        } else {
            // Album shelf
            Album[] albums = loadAlbums();

            for (Album album : albums) {
                ImageView albumCover = new ImageView(album.getCoverImage());
                albumCover.setFitHeight(50);
                albumCover.setFitWidth(50);
                albumCover.setPreserveRatio(true);

                //Create an overlay effect when mouse hovers over the album cover
                albumCover.setOnMouseEntered(e -> albumCover.setEffect(new DropShadow()));
                albumCover.setOnMouseExited(e -> albumCover.setEffect(null));

                // Event Listener for MouseClick
                albumCover.setOnMouseClicked(e -> {
                    FocusView.getInstance().setSongCollection(album);
                });

                //Create Label for the album name
                Label albumLabel = new Label(album.getName());
                albumLabel.setWrapText(true);
                albumLabel.setTextAlignment(TextAlignment.CENTER);

                //Create VBox and add both ImageView and Label
                VBox albumBox = new VBox(albumCover, albumLabel);
                albumBox.setAlignment(Pos.CENTER);

                this.vBox.getChildren().add(albumBox);
            }

        }
    }

    public void initialize(Stage primaryStage) {
        initializeComponents(primaryStage);
    }

    public VBox getVBox() {
        return this.vBox;
    }

}