package openaudio.views;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.controllers.CollectionController;
import openaudio.controllers.MusicPlayerController;
import openaudio.controllers.QueueController;
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


public class FocusView {
    private SongCollection songCollection;
    private VBox vBox;
    private boolean moving;
    private HashMap<Song, Integer> interactionMap;

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
        this.interactionMap = new HashMap<Song, Integer>();
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
        albumCover.fitWidthProperty().bind(this.vBox.heightProperty().divide(6));
        albumCover.fitHeightProperty().bind(this.vBox.heightProperty().divide(6));
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

        VBox songVBox = new VBox();

        for (Song song : this.songCollection.getSongs()) {
            HBox hBox = new HBox();
            Button songButton = new Button(song.getTitle());
            songButton.setOnAction(event -> {
                MusicPlayerController.getInstance().playSong(song);
                List<Song> remainingSongs = this.songCollection.getRemainingSongs(song);
                QueueController.getInstance().setCollection(remainingSongs);
                QueueController.getInstance().clearFutureQueue();
            });
            songButton.getStyleClass().add("song-button");
            hBox.getChildren().add(songButton);

            Button addToQueueButton = new Button();
            ImageView addToQueueView = new ImageView(addToQueueImage);
            addToQueueView.setFitHeight(width / 120);
            addToQueueView.setFitWidth(width / 120);
            addToQueueButton.setGraphic(addToQueueView);

            addToQueueButton.setOnAction(event -> {
                QueueController.getInstance().addUserSong(song);
            });
            addToQueueButton.getStyleClass().add("generic-button");
            hBox.getChildren().add(addToQueueButton);

            if (!(this.songCollection instanceof Playlist)) {
                Button addToPlaylistButton = new Button();
                ImageView addPlaylistView = new ImageView(addPlaylistImage);
                addPlaylistView.setFitHeight(width / 120);
                addPlaylistView.setFitWidth(width / 120);
                addToPlaylistButton.setGraphic(addPlaylistView);

                addToPlaylistButton.setOnAction(event -> {
                    if (this.interactionMap.get(song) == null || this.interactionMap.get(song) == 0) {
                        this.interactionMap.put(song, 1);
                    } else {
                        this.interactionMap.put(song, 0);
                    }
                    this.display();
                });
                addToPlaylistButton.getStyleClass().add("generic-button");
                hBox.getChildren().add(addToPlaylistButton);

                if (this.interactionMap.get(song) != null && this.interactionMap.get(song) == 1) {
                    ArrayList<Playlist> playlists = CollectionController.getInstance().getPlaylists();
                    for (Playlist playlist : playlists) {
                        Button playlistButton = new Button(playlist.getName());
                        playlistButton.setOnAction(event2 -> {
                            playlist.addSong(song);
                        });
                        playlistButton.getStyleClass().add("generic-button");
                        hBox.getChildren().add(playlistButton);
                    }

                    // Make a new playlist
                    Button newPlaylistButton = new Button("New Playlist");
                    newPlaylistButton.getStyleClass().add("generic-button");
                    newPlaylistButton.setOnAction(event3 -> {
                        TextInputDialog dialog = new TextInputDialog("New Playlist");
                        dialog.setTitle("New Playlist");
                        dialog.setHeaderText("Enter a name for the new playlist");
                        dialog.setContentText("Name:");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()){
                            Playlist newPlaylist = new Playlist(result.get());
                            newPlaylist.addSong(song);
                            CollectionController.getInstance().addPlaylist(newPlaylist);
                        }
                    });
                    hBox.getChildren().add(newPlaylistButton);
                }
                    
                // Move up button
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
                    hBox.getChildren().add(moveUpButton);
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
                    hBox.getChildren().add(moveDownButton);
                }
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
            songVBox.getChildren().add(hBox);

            songCount++;
        }

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

        songVBox.getChildren().add(moveButton);

        ScrollPane scrollPane = new ScrollPane(songVBox);
        scrollPane.getStyleClass().add("scroll-pane");

        this.vBox.getChildren().add(scrollPane);
    }
}