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
        this.vBox.getChildren().clear();
        for (Song song : this.songCollection.getSongs()) {
            HBox hBox = new HBox();
            Button songButton = new Button(song.getTitle());
            songButton.setOnAction(event -> {
                MusicPlayerController.getInstance().playSong(song);
                List<Song> remainingSongs = this.songCollection.getRemainingSongs(song);
                QueueController.getInstance().setCollection(remainingSongs);
            });
            hBox.getChildren().add(songButton);

            Button addToQueueButton = new Button("Add to Queue");
            addToQueueButton.setOnAction(event -> {
                QueueController.getInstance().addUserSong(song);
            });
            hBox.getChildren().add(addToQueueButton);

            Button addToPlaylistButton = new Button("Add to Playlist");
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
                

            // Button moveUpButton = new Button("Move Up");
            // moveUpButton.setOnAction(event -> {
            //     this.songCollection.moveUp(song);
            // });
            // hBox.getChildren().add(moveUpButton);

            // Button moveDownButton = new Button("Move Down");
            // moveDownButton.setOnAction(event -> {
            //     this.songCollection.moveDown(song);
            // });
            // hBox.getChildren().add(moveDownButton);

            this.vBox.getChildren().add(hBox);
        }
    }
}