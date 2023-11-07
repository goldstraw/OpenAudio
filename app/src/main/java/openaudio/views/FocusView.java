package openaudio.views;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.controllers.CollectionController;
import openaudio.controllers.MusicPlayerController;
import openaudio.controllers.QueueController;
import openaudio.components.AddToPlaylistButton;
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

        Image addToQueueImage = new Image(getClass().getResourceAsStream("/img/add-to-queue.png"));
        Image addPlaylistImage = new Image(getClass().getResourceAsStream("/img/add-to-playlist.png"));
        Image moveUpImage = new Image(getClass().getResourceAsStream("/img/move-up.png"));
        Image moveDownImage = new Image(getClass().getResourceAsStream("/img/move-down.png"));
        Image removeImage = new Image(getClass().getResourceAsStream("/img/remove-icon.png"));
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
            Button songButton = new Button(song.getTitle());
            songButton.setOnAction(event -> {
                MusicPlayerController.getInstance().playSong(song);
                List<Song> remainingSongs = this.songCollection.getRemainingSongs(song);
                QueueController.getInstance().setCollection(remainingSongs);
                QueueController.getInstance().clearFutureQueue();
            });
            songButton.getStyleClass().add("song-button");
            songVBox.getChildren().add(songButton);

            Button addToQueueButton = new Button();
            ImageView addToQueueView = new ImageView(addToQueueImage);
            addToQueueView.setFitHeight(width / 120);
            addToQueueView.setFitWidth(width / 120);
            addToQueueButton.setGraphic(addToQueueView);

            addToQueueButton.setOnAction(event -> {
                QueueController.getInstance().addUserSong(song);
            });
            addToQueueButton.getStyleClass().add("generic-button");
            addToQueueButton.getStyleClass().add("grey-bg");

            hBox.getChildren().add(addToQueueButton);
            songVBox.getChildren().add(hBox);

            if (!(this.songCollection instanceof Playlist)) {
                AddToPlaylistButton addToPlaylistButton = new AddToPlaylistButton(song);
                hBox.getChildren().add(addToPlaylistButton);
                    
                // Move up button
                HBox moveButtonsHBox = new HBox();
                if (songCount > 0 && this.moving) {
                    Button moveUpButton = new Button();
                    ImageView moveUpView = new ImageView(moveUpImage);
                    moveUpView.setFitHeight(width / 120);
                    moveUpView.setFitWidth(width / 120);
                    moveUpButton.setGraphic(moveUpView);

                    moveUpButton.setOnAction(event -> {
                        this.songCollection.moveUp(song);
                        this.display();
                    });
                    moveUpButton.getStyleClass().add("generic-button");
                    moveUpButton.getStyleClass().add("grey-bg");
                    moveButtonsHBox.getChildren().add(moveUpButton);
                }

                // Move down button
                if (songCount < this.songCollection.getSongs().size() - 1 && this.moving) {
                    Button moveDownButton = new Button();
                    ImageView moveDownView = new ImageView(moveDownImage);
                    moveDownView.setFitHeight(width / 120);
                    moveDownView.setFitWidth(width / 120);
                    moveDownButton.setGraphic(moveDownView);
                    
                    moveDownButton.setOnAction(event -> {
                        this.songCollection.moveDown(song);
                        this.display();
                    });
                    moveDownButton.getStyleClass().add("generic-button");
                    moveDownButton.getStyleClass().add("grey-bg");
                    moveButtonsHBox.getChildren().add(moveDownButton);
                }
                songVBox.getChildren().add(moveButtonsHBox);
            } else {
                Button removeFromPlaylistButton = new Button();

                ImageView removeView = new ImageView(removeImage);
                removeView.setFitHeight(width / 120);
                removeView.setFitWidth(width / 120);
                removeFromPlaylistButton.setGraphic(removeView);
                removeFromPlaylistButton.setOnAction(event -> {
                    Playlist playlist = (Playlist) this.songCollection;
                    playlist.removeSong(song);
                    this.display();
                });
                removeFromPlaylistButton.getStyleClass().add("generic-button");
                hBox.getChildren().add(removeFromPlaylistButton);
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