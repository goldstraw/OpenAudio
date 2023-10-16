package openaudio.controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import openaudio.utils.Settings;
import openaudio.models.Album;
import javafx.scene.control.Label;
import java.io.File;


public class AlbumController {

    private VBox vBox;
    private Button chooseDirButton;

    private Album[] loadAlbums() {
        // Load albums from music folder
        // For each folder in music folder, create an album object
        // Return array of albums

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
                Label albumLabel = new Label(album.getName());
                this.vBox.getChildren().add(albumLabel);
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