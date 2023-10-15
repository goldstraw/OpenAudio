package openaudio.controllers;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.VBox;
import openaudio.models.Song;


import javafx.animation.*;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicBoolean;


public class MusicPlayerController {

    private VBox vBox;
    private Button playButton;
    private Label songLabel;
    private DoubleProperty currentSongSeconds;

    private MediaPlayer mediaPlayer;
    private AtomicBoolean userIsDraggingSlider = new AtomicBoolean(false);
    private Timeline sliderUpdater;
    private Slider slider;

    public void playSong(Song song) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer = new MediaPlayer(new Media(song.getFilePath()));

        this.mediaPlayer.setOnReady(() -> {
            this.slider.setMax(this.mediaPlayer.getMedia().getDuration().toSeconds());
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            this.currentSongSeconds.set(newValue.toSeconds());
        });

        mediaPlayer.play();
    }

    public void initialize() {
        this.vBox = new VBox();
        this.slider = new Slider();
        vBox.getChildren().add(this.slider);

        this.playButton = new Button();
        vBox.getChildren().add(this.playButton);

        this.songLabel = new Label("OK Computer by Radiohead");
        vBox.getChildren().add(this.songLabel);

        this.currentSongSeconds = new SimpleDoubleProperty();

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

    public VBox getVBox() {
        return this.vBox;
    }

}