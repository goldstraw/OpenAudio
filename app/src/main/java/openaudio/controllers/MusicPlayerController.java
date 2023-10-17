package openaudio.controllers;

import openaudio.models.Song;
import openaudio.controllers.QueueController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.geometry.Pos;
import javafx.animation.*;
import javafx.util.Duration;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;


// Singleton
public class MusicPlayerController {

    private VBox vBox;
    private Button playButton;
    private Button nextButton;
    private Button previousButton;
    private Label songLabel;
    private DoubleProperty currentSongSeconds;
    private MediaPlayer mediaPlayer;
    private AtomicBoolean userIsDraggingSlider = new AtomicBoolean(false);
    private Timeline sliderUpdater;
    private Slider slider;
    private ImageView playPauseView;
    private Song currentSong;
    
    private final Image playImage = new Image(getClass().getResourceAsStream("/img/play-icon.png"));
    private final Image pauseImage = new Image(getClass().getResourceAsStream("/img/pause-icon.png"));

    private static MusicPlayerController instance = null;

    public static MusicPlayerController getInstance() {
        if (instance == null) {
            instance = new MusicPlayerController();
        }
        return instance;
    }

    private MusicPlayerController() {
    }

    public void playSong(Song song) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        this.songLabel.setText(song.getTitle());
        File file = new File(song.getFilePath());
        this.mediaPlayer = new MediaPlayer(new Media(file.toURI().toString()));

        this.mediaPlayer.setOnReady(() -> {
            this.slider.setMax(this.mediaPlayer.getMedia().getDuration().toSeconds());
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            this.currentSongSeconds.set(newValue.toSeconds());
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            Song nextSong = QueueController.getInstance().getNextSong();
            playSong(nextSong);
        });

        this.playPauseView.setImage(pauseImage);
        mediaPlayer.play();

        this.currentSong = song;
    }

    private void initializeComponents() {
        Screen screen = Screen.getPrimary();
        double width = screen.getVisualBounds().getWidth();

        this.vBox = new VBox();
        this.vBox.setAlignment(Pos.BOTTOM_CENTER);

        this.playButton = new Button();
        vBox.getChildren().add(this.playButton);
        this.playPauseView = new ImageView(this.playImage);
        this.playPauseView.setFitHeight(width / 80);
        this.playPauseView.setFitWidth(width / 80);
        this.playButton.setGraphic(playPauseView);

        this.playButton.setOnAction(event -> {
            if (playPauseView.getImage().equals(playImage)) {
                playPauseView.setImage(pauseImage);
                this.mediaPlayer.play();
            } else {
                playPauseView.setImage(playImage);
                this.mediaPlayer.pause();
            }
        });

        this.nextButton = new Button("Next");
        vBox.getChildren().add(this.nextButton);
        this.nextButton.setOnAction(event -> {
            QueueController.getInstance().addSongToHistory(this.currentSong);
            playSong(QueueController.getInstance().getNextSong());
        });

        this.previousButton = new Button("Previous");
        vBox.getChildren().add(this.previousButton);
        this.previousButton.setOnAction(event -> {
            Song previousSong = QueueController.getInstance().getPreviousSong();
            QueueController.getInstance().addUserSongToFront(this.currentSong);
            if (previousSong != null) {
                playSong(previousSong);
            }
        });

        this.slider = new Slider();
        vBox.getChildren().add(this.slider);

        this.songLabel = new Label("OK Computer by Radiohead");
        vBox.getChildren().add(this.songLabel);

        this.currentSongSeconds = new SimpleDoubleProperty();
    }

    private void configureSlider() {
        slider.setOnMousePressed(event -> {
            userIsDraggingSlider.set(true);
        });

        slider.setOnMouseReleased(event -> {
            userIsDraggingSlider.set(false);
            mediaPlayer.seek(javafx.util.Duration.seconds(slider.getValue()));
            this.currentSongSeconds.set(slider.getValue());
        });

        // timeline that updates slider (if not dragged by user)
        sliderUpdater = new Timeline(
            new KeyFrame(
                Duration.seconds(0),
                event -> {
                    if (!userIsDraggingSlider.get()) {
                        slider.setValue(this.currentSongSeconds.get());
                    }
                }
            ),
            new KeyFrame(Duration.seconds(0.1))
        );
        sliderUpdater.setCycleCount(Timeline.INDEFINITE);
        sliderUpdater.play();
    }

    public void initialize() {
        initializeComponents();
        configureSlider();
    }

    public VBox getVBox() {
        return this.vBox;
    }

}