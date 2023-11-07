package openaudio.controllers;

import openaudio.utils.Settings;
import openaudio.models.Album;
import openaudio.models.Playlist;
import openaudio.views.FocusView;
import openaudio.models.Song;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextAlignment;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;


public class CollectionController {

    private VBox albumShelf;
    private VBox playlistShelf;
    private Button chooseDirButton;
    private ArrayList<Album> albums;
    private ArrayList<Playlist> playlists;

    private static CollectionController instance = null;

    public static CollectionController getInstance() {
        if (instance == null) {
            instance = new CollectionController();
        }
        return instance;
    }

    private CollectionController() {
    }

    private void loadCollections() {
        String musicFolder = Settings.getInstance().musicFolder;
        String[] folders = new File(musicFolder).list();

        ArrayList<Album> albums = new ArrayList<Album>();
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();

        for (int i = 0; i < folders.length; i++) {
            // Check if the folder is an album
            String[] folderFiles = new File(musicFolder + "/" + folders[i]).list();
            if (folderFiles == null) {
                continue;
            }
            for (int j = 0; j < folderFiles.length; j++) {
                if (folderFiles[j].endsWith(".mp3") || folderFiles[j].endsWith(".wav")) {
                    Album newAlbum = new Album(folders[i]);
                    albums.add(newAlbum);
                    break;
                }
            }

            // Check if the folder is a playlist
            for (int j = 0; j < folderFiles.length; j++) {
                if (folderFiles[j].endsWith(".playlistinfo")) {
                    playlists.add(new Playlist(folders[i]));
                    break;
                }
            }
        }

        this.albums = albums;
        this.playlists = playlists;
    }

    private void createAlbumShelf() {
        VBox scrollVBox = new VBox();
        for (Album album : albums) {
            ImageView albumCover = new ImageView(album.getCoverImage());
            Screen screen = Screen.getPrimary();
            double height = screen.getVisualBounds().getHeight();
            albumCover.setFitHeight(height / 12);
            albumCover.setFitWidth(height / 12);
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

            scrollVBox.getChildren().add(albumBox);
        }
        ScrollPane scrollPane = new ScrollPane(scrollVBox);
        this.albumShelf.getChildren().add(scrollPane);
    }

    private void createPlaylistShelf() {
        VBox scrollVBox = new VBox();
        for (Playlist playlist : playlists) {
            ImageView playlistCover = new ImageView(playlist.getCoverImage());
            playlistCover.fitWidthProperty().bind(this.playlistShelf.heightProperty().divide(12));
            playlistCover.fitHeightProperty().bind(this.playlistShelf.heightProperty().divide(12));
            playlistCover.setPreserveRatio(true);

            //Create an overlay effect when mouse hovers over the album cover
            playlistCover.setOnMouseEntered(e -> playlistCover.setEffect(new DropShadow()));
            playlistCover.setOnMouseExited(e -> playlistCover.setEffect(null));

            // Event Listener for MouseClick
            playlistCover.setOnMouseClicked(e -> {
                Set<Song> playlistContenders = FocusView.getInstance().getPlaylistContenders();
                if (playlistContenders.size() > 0) {
                    for (Song playlistContender : playlistContenders) {
                        playlist.addSong(playlistContender);
                    }
                    FocusView.getInstance().clearPlaylistContenders();
                } else {
                    FocusView.getInstance().setSongCollection(playlist);
                }
            });

            //Create Label for the album name
            Label playlistLabel = new Label(playlist.getName());
            playlistLabel.setWrapText(true);
            playlistLabel.setTextAlignment(TextAlignment.CENTER);

            //Create VBox and add both ImageView and Label
            VBox playlistBox = new VBox(playlistCover, playlistLabel);
            playlistBox.setAlignment(Pos.CENTER);

            scrollVBox.getChildren().add(playlistBox);
        }
        ScrollPane scrollPane = new ScrollPane(scrollVBox);
        this.playlistShelf.getChildren().add(scrollPane);
    }

    public void initialize(Stage primaryStage) {
        this.albumShelf = new VBox();
        this.albumShelf.setAlignment(Pos.CENTER);
        this.playlistShelf = new VBox();
        this.playlistShelf.setAlignment(Pos.CENTER);

        if (Settings.getInstance().musicFolder == null) {
            this.chooseDirButton = new Button("Choose Music Directory");
            this.albumShelf.getChildren().add(this.chooseDirButton);

            this.chooseDirButton.setOnAction(event -> {
                Settings.getInstance().chooseDirectory(primaryStage, this.chooseDirButton);
            });
        } else {
            // Album shelf
            loadCollections();
            createAlbumShelf();
            createPlaylistShelf();

        }
    }

    public VBox getAlbumShelf() {
        return this.albumShelf;
    }

    public VBox getPlaylistShelf() {
        return this.playlistShelf;
    }

    public ArrayList<Album> getAlbums() {
        return this.albums;
    }

    public ArrayList<Playlist> getPlaylists() {
        return this.playlists;
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
        // Create folder for playlist with .playlistinfo extension
        String musicFolder = Settings.getInstance().musicFolder;
        String playlistFolder = musicFolder + "/" + playlist.getName();
        File file = new File(playlistFolder);
        file.mkdir();

        // Create .playlistinfo file
        try {
            File playlistInfo = new File(playlistFolder + "/" + playlist.getName() + ".playlistinfo");
            playlistInfo.createNewFile();
            // Write song file path and .playlistinfo file
            PrintWriter writer = new PrintWriter(playlistInfo, "UTF-8");
            for (Song song : playlist.getSongs()) {
                writer.println(song.getFilePath());
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("Error creating playlist info file");
        }
    }

}