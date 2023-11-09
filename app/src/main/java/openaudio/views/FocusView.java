package openaudio.views;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.controllers.CollectionController;
import openaudio.controllers.MusicPlayerController;
import openaudio.controllers.QueueController;
import openaudio.components.AddToPlaylistButton;
import openaudio.components.AddToQueueButton;
import openaudio.components.SongButton;
import openaudio.components.MoveUpButton;
import openaudio.components.MoveDownButton;
import openaudio.components.RemoveFromPlaylistButton;
import openaudio.App;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import openaudio.models.Playlist;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class FocusView {
    private SongCollection songCollection;
    private VBox vBox;
    private boolean moving;
    private Set<Song> playlistContenders = new HashSet<Song>();

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

    public Set<Song> getPlaylistContenders() {
        return this.playlistContenders;
    }

    public void addPlaylistContender(Song song) {
        this.playlistContenders.add(song);
    }

    public void removePlaylistContender(Song song) {
        this.playlistContenders.remove(song);
    }

    public void clearPlaylistContenders() {
        this.playlistContenders.clear();
        this.display();
    }
    
    public VBox getVBox() {
        return this.vBox;
    }

    public void setSongCollection(SongCollection songCollection) {
        this.songCollection = songCollection;
        this.display();
    }

    public void display() {
        Screen screen = Screen.getPrimary();
        double width = screen.getVisualBounds().getWidth();
        double height = screen.getVisualBounds().getHeight();

        this.vBox.getChildren().clear();
        int songCount = 0;

        Image moveImage = new Image(getClass().getResourceAsStream("/img/arrow-move-icon.png"));

        // Title
        HBox titleBox = new HBox();
        ImageView albumCover = new ImageView(this.songCollection.getCoverImage());
        albumCover.setFitHeight(height / 6);
        albumCover.setFitWidth(height / 6);
        albumCover.getStyleClass().add("album-cover");
        titleBox.getChildren().add(albumCover);

        VBox titleBox2 = new VBox();

        Label title = new Label(this.songCollection.getName());
        title.getStyleClass().add("title");
        titleBox2.getChildren().add(title);

        Label artistLabel = new Label(this.songCollection.getArtist());
        artistLabel.getStyleClass().add("artist-label");
        titleBox2.getChildren().add(artistLabel);

        titleBox.getChildren().add(titleBox2);
        titleBox.getStyleClass().add("title-box");

        this.vBox.getChildren().add(titleBox);

        VBox collectionVBox = new VBox();
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(50);
        flowPane.setVgap(20);
        // Adjust wrap length based on window size
        flowPane.prefWrapLengthProperty().bind(App.getInstance().getStage().widthProperty().subtract(200));


        for (Song song : this.songCollection.getSongs()) {
            VBox songVBox = new VBox();
            HBox hBox = new HBox();
            songVBox.getChildren().add(new SongButton(song, this.songCollection));

            AddToQueueButton addToQueueButton = new AddToQueueButton(song);
            hBox.getChildren().add(addToQueueButton);
            songVBox.getChildren().add(hBox);

            if (!(this.songCollection instanceof Playlist)) {
                AddToPlaylistButton addToPlaylistButton = new AddToPlaylistButton(song);
                hBox.getChildren().add(addToPlaylistButton);
                    
                // Move up button
                HBox moveButtonsHBox = new HBox();
                if (songCount > 0 && this.moving) {
                    MoveUpButton moveUpButton = new MoveUpButton(song, this.songCollection);
                    moveButtonsHBox.getChildren().add(moveUpButton);
                }

                // Move down button
                if (songCount < this.songCollection.getSongs().size() - 1 && this.moving) {
                    MoveDownButton moveDownButton = new MoveDownButton(song, this.songCollection);
                    moveButtonsHBox.getChildren().add(moveDownButton);
                }
                songVBox.getChildren().add(moveButtonsHBox);
            } else {
                RemoveFromPlaylistButton rfmButton = new RemoveFromPlaylistButton(song, this.songCollection);
                hBox.getChildren().add(rfmButton);
            }
            
            flowPane.getChildren().add(songVBox);

            songCount++;
        }
        collectionVBox.getChildren().add(flowPane);
        
        Button moveButton = new Button();
        ImageView moveView = new ImageView(moveImage);
        moveView.setFitHeight(width / 120);
        moveView.setFitWidth(width / 120);
        moveButton.setGraphic(moveView);
        moveButton.setOnAction(event -> {
            this.moving = !this.moving;
            this.display();
        });
        moveButton.getStyleClass().add("generic-button");

        collectionVBox.getChildren().add(moveButton);

        ScrollPane scrollPane = new ScrollPane(collectionVBox);
        scrollPane.getStyleClass().add("scroll-pane");

        this.vBox.getChildren().add(scrollPane);
    }
}