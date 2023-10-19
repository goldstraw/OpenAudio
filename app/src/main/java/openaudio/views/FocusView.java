package openaudio.views;

import openaudio.models.SongCollection;
import openaudio.models.Song;
import openaudio.controllers.CollectionController;
import openaudio.controllers.MusicPlayerController;
import openaudio.controllers.QueueController;
import javafx.scene.control.Button;
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
        Screen screen = Screen.getPrimary();
        double width = screen.getVisualBounds().getWidth();

        this.vBox.getChildren().clear();
        int songCount = 0;
        for (Song song : this.songCollection.getSongs()) {
            HBox hBox = new HBox();
            Button songButton = new Button(song.getTitle());
            songButton.setOnAction(event -> {
                MusicPlayerController.getInstance().playSong(song);
                List<Song> remainingSongs = this.songCollection.getRemainingSongs(song);
                QueueController.getInstance().setCollection(remainingSongs);
            });
            hBox.getChildren().add(songButton);

            Button addToQueueButton = new Button();
            ImageView addToQueueView = new ImageView(new Image(getClass().getResourceAsStream("/img/add-to-queue.png")));
            addToQueueView.setFitHeight(width / 120);
            addToQueueView.setFitWidth(width / 120);
            addToQueueButton.setGraphic(addToQueueView);

            addToQueueButton.setOnAction(event -> {
                QueueController.getInstance().addUserSong(song);
            });
            hBox.getChildren().add(addToQueueButton);

            Button addToPlaylistButton = new Button();
            ImageView addPlaylistView = new ImageView(new Image(getClass().getResourceAsStream("/img/add-to-playlist.png")));
            addPlaylistView.setFitHeight(width / 120);
            addPlaylistView.setFitWidth(width / 120);
            addToPlaylistButton.setGraphic(addPlaylistView);

            addToPlaylistButton.setOnAction(event -> {
                ArrayList<Playlist> playlists = CollectionController.getInstance().getPlaylists();
                for (Playlist playlist : playlists) {
                    Button playlistButton = new Button(playlist.getName());
                    playlistButton.setOnAction(event2 -> {
                        playlist.addSong(song);
                    });
                    hBox.getChildren().add(playlistButton);
                }

                // Make a new playlist
                Button newPlaylistButton = new Button("New Playlist");
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
            });
            hBox.getChildren().add(addToPlaylistButton);
                
            // Move up button
            if (songCount > 0) {
                Button moveUpButton = new Button();
                ImageView moveUpView = new ImageView(new Image(getClass().getResourceAsStream("/img/move-up.png")));
                moveUpView.setFitHeight(width / 120);
                moveUpView.setFitWidth(width / 120);
                moveUpButton.setGraphic(moveUpView);

                moveUpButton.setOnAction(event -> {
                    this.songCollection.moveUp(song);
                    this.display();
                });
                hBox.getChildren().add(moveUpButton);
            }

            // Move down button
            if (songCount < this.songCollection.getSongs().size() - 1) {
                Button moveDownButton = new Button();
                ImageView moveDownView = new ImageView(new Image(getClass().getResourceAsStream("/img/move-down.png")));
                moveDownView.setFitHeight(width / 120);
                moveDownView.setFitWidth(width / 120);
                moveDownButton.setGraphic(moveDownView);
                
                moveDownButton.setOnAction(event -> {
                    this.songCollection.moveDown(song);
                    this.display();
                });
                hBox.getChildren().add(moveDownButton);
            }

            // Remove from playlist button
            if (this.songCollection instanceof Playlist) {
                Button removeFromPlaylistButton = new Button("Remove from Playlist");
                removeFromPlaylistButton.setOnAction(event -> {
                    Playlist playlist = (Playlist) this.songCollection;
                    playlist.removeSong(song);
                    this.display();
                });
                hBox.getChildren().add(removeFromPlaylistButton);
            }

            this.vBox.getChildren().add(hBox);
            songCount++;
        }
    }
}