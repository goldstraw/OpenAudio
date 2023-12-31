package openaudio.controllers;

import openaudio.models.Song;
import openaudio.controllers.QueueController;
import openaudio.components.ShuffleButton;
import openaudio.components.RepeatButton;
import openaudio.utils.Settings;
import openaudio.utils.Statistics;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
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
    private VBox songInfo;
    private Label songLabel;
    private Label albumArtistLabel;
    private Label timeElapsedLabel;
    private DoubleProperty currentSongSeconds;
    private MediaPlayer mediaPlayer;
    private AtomicBoolean userIsDraggingSlider = new AtomicBoolean(false);
    private Timeline sliderUpdater;
    private Slider slider;
    private ImageView playPauseView;
    private Song currentSong;
    
    private final Image playImage = new Image(getClass().getResourceAsStream("/img/play-icon.png"));
    private final Image pauseImage = new Image(getClass().getResourceAsStream("/img/pause-icon.png"));
    private final Image nextImage = new Image(getClass().getResourceAsStream("/img/step-forward-icon.png"));
    private final Image previousImage = new Image(getClass().getResourceAsStream("/img/step-backward-icon.png"));
    private final Image darkModeImage = new Image(getClass().getResourceAsStream("/img/dark-mode-icon.png"));

    private static MusicPlayerController instance = null;

    public static MusicPlayerController getInstance() {
        if (instance == null) {
            instance = new MusicPlayerController();
        }
        return instance;
    }

    private MusicPlayerController() {
    }

    public Song getSong() {
        return this.currentSong;
    }

    public void playSong(Song song) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            Statistics.getInstance().writeStats(this.currentSong, this.currentSongSeconds.get());
        }

        this.songLabel.setText(song.getTitle());
        this.albumArtistLabel.setText(song.getAlbum() + " - " + song.getArtist());
        File file = new File(song.getFilePath());
        this.mediaPlayer = new MediaPlayer(new Media(file.toURI().toString()));

        this.mediaPlayer.setOnReady(() -> {
            this.slider.setMax(this.mediaPlayer.getMedia().getDuration().toSeconds());
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            this.currentSongSeconds.set(newValue.toSeconds());
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            boolean repeat = Settings.getInstance().getRepeat();
            if (repeat) {
                this.currentSongSeconds.set(0);
                mediaPlayer.play();
            } else {
                Song nextSong = QueueController.getInstance().getNextSong();
                playSong(nextSong);
            }
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

        // To have the player in the middle, with other components on the sides, we need to use a GridPane
        // with 3 columns, and the player in the middle column.
        // The left and right columns will be empty, but will have a width of 1/3 of the screen width.
        // The player will have a width of 1/3 of the screen width.

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(width);

        HBox playerHBox = new HBox();
        borderPane.setCenter(playerHBox);

        Button darkModeButton = new Button();
        ImageView darkModeView = new ImageView(this.darkModeImage);
        darkModeView.setFitHeight(width / 80);
        darkModeView.setFitWidth(width / 80);
        darkModeButton.setGraphic(darkModeView);
        darkModeButton.setOnAction(event -> {
            Settings.getInstance().toggleDarkMode();
        });
        borderPane.setRight(darkModeButton);

        Button darkModeButton2 = new Button();
        ImageView darkModeView2 = new ImageView(this.darkModeImage);
        darkModeView2.setFitHeight(width / 80);
        darkModeView2.setFitWidth(width / 80);
        darkModeButton2.setGraphic(darkModeView2);
        borderPane.setLeft(darkModeButton2);


        this.previousButton = new Button();
        previousButton.getStyleClass().add("grey-bg");
        ImageView previousView = new ImageView(this.previousImage);
        previousView.setFitHeight(width / 80);
        previousView.setFitWidth(width / 80);
        this.previousButton.setGraphic(previousView);

        RepeatButton repeatButton = new RepeatButton();
        playerHBox.getChildren().add(repeatButton);

        vBox.getChildren().add(this.previousButton);
        this.previousButton.setOnAction(event -> {
            if (this.mediaPlayer == null) {
                return;
            }
            Song previousSong = QueueController.getInstance().getPreviousSong();
            QueueController.getInstance().addFutureSong(this.currentSong);
            if (previousSong != null) {
                playSong(previousSong);
            }
        });
        playerHBox.getChildren().add(this.previousButton);

        this.playButton = new Button();
        this.playButton.getStyleClass().add("grey-bg");

        this.playPauseView = new ImageView(this.playImage);
        this.playPauseView.setFitHeight(width / 80);
        this.playPauseView.setFitWidth(width / 80);
        this.playButton.setGraphic(playPauseView);

        this.playButton.setOnAction(event -> {
            if (this.mediaPlayer == null) {
                return;
            }
            if (playPauseView.getImage().equals(playImage)) {
                playPauseView.setImage(pauseImage);
                this.mediaPlayer.play();
            } else {
                playPauseView.setImage(playImage);
                this.mediaPlayer.pause();
            }
        });

        playerHBox.getChildren().add(this.playButton);

        this.nextButton = new Button();
        nextButton.getStyleClass().add("grey-bg");
        ImageView nextView = new ImageView(this.nextImage);
        nextView.setFitHeight(width / 80);
        nextView.setFitWidth(width / 80);
        this.nextButton.setGraphic(nextView);

        vBox.getChildren().add(this.nextButton);
        this.nextButton.setOnAction(event -> {
            if (this.mediaPlayer == null) {
                return;
            }
            QueueController.getInstance().addSongToHistory(this.currentSong);
            playSong(QueueController.getInstance().getNextSong());
        });
        playerHBox.getChildren().add(this.nextButton);

        ShuffleButton shuffleButton = new ShuffleButton();
        playerHBox.getChildren().add(shuffleButton);

        playerHBox.getStyleClass().add("player-hbox");
        this.vBox.getChildren().add(borderPane);

        this.slider = new Slider();
        vBox.getChildren().add(this.slider);

        this.songInfo = new VBox();

        this.songLabel = new Label("");
        this.songInfo.getChildren().add(this.songLabel);


        this.albumArtistLabel = new Label("");
        this.songInfo.getChildren().add(this.albumArtistLabel);

        this.songLabel.getStyleClass().add("generic-dark");
        this.albumArtistLabel.getStyleClass().add("generic-dark");
        this.songInfo.getStyleClass().add("song-info");

        this.vBox.getChildren().add(this.songInfo);

        this.timeElapsedLabel = new Label("0:00 / 0:00");
        this.timeElapsedLabel.getStyleClass().add("generic-dark");
        this.vBox.getChildren().add(this.timeElapsedLabel);
        this.timeElapsedLabel.getStyleClass().add("time-elapsed");

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
                        int minutes = (int) this.currentSongSeconds.get() / 60;
                        int seconds = (int) this.currentSongSeconds.get() % 60;
                        String timeElapsed = String.format("%d:%02d", minutes, seconds);
                        if (this.mediaPlayer == null) {
                            this.timeElapsedLabel.setText(timeElapsed + " / 0:00");
                            return;
                        } else {
                            int totalMinutes = (int) this.mediaPlayer.getMedia().getDuration().toSeconds() / 60;
                            int totalSeconds = (int) this.mediaPlayer.getMedia().getDuration().toSeconds() % 60;
                            String totalTime = String.format("%d:%02d", totalMinutes, totalSeconds);
                            this.timeElapsedLabel.setText(timeElapsed + " / " + totalTime);
                        }
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