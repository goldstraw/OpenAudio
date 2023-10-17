package openaudio.views;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import openaudio.controllers.MusicPlayerController;


public class FocusView {
    private SongCollection songCollection;
    private VBox vBox;

    private static FocusView instance = null;

    public static FocusView getInstance() {
        if (instance == null) {
            instance = new FocusView();
        }
        return instance;
    }

    private FocusView() {
        this.vBox = new VBox();
        this.vBox.setAlignment(Pos.CENTER);
    }
    
    public VBox getVBox() {
        return this.vBox;
    }

    public void setSongCollection(SongCollection songCollection) {
        this.songCollection = songCollection;
        this.display();
    }

    public void display() {
        this.vBox.getChildren().clear();
        for (Song song : this.songCollection.getSongs()) {
            Button songButton = new Button(song.getTitle());
            songButton.setOnAction(event -> {
                MusicPlayerController.getInstance().playSong(song);
            });
            this.vBox.getChildren().add(songButton);
        }
    }
}