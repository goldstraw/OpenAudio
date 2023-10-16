package openaudio.controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import openaudio.utils.Settings;


public class AlbumController {

    private VBox vBox;
    private Button chooseDirButton;

    private void initializeComponents(Stage primaryStage) {
        this.vBox = new VBox();
        this.vBox.setAlignment(Pos.CENTER);

        
        this.chooseDirButton = new Button("Choose Music Directory");
        vBox.getChildren().add(this.chooseDirButton);
        this.chooseDirButton.setOnAction(event -> {
            Settings.getInstance().chooseDirectory(primaryStage);
        });
    }

    public void initialize(Stage primaryStage) {
        initializeComponents(primaryStage);
    }

    public VBox getVBox() {
        return this.vBox;
    }

}