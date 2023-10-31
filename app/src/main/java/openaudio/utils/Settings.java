package openaudio.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Settings {

    private static Settings instance = null;

    public String musicFolder;
    private final String propertiesFileName = "settings.properties";
    private Properties properties = new Properties();

    private Settings() {
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public void readProperties() {
        try {
            File file = new File(propertiesFileName);
            if(file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                properties.load(inputStream);
                musicFolder = properties.getProperty("musicFolder");
                inputStream.close();
            }
        } catch (Exception e) {
            System.out.println("Error reading properties: " + e.getMessage());
        }
    }

    public void chooseDirectory(Stage primaryStage, Button chooseDirButton) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Music Directory");

        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if(selectedDirectory != null) {
            musicFolder = selectedDirectory.getAbsolutePath();
            properties.setProperty("musicFolder", musicFolder);

            try {
                OutputStream outputStream = new FileOutputStream(propertiesFileName);
                properties.store(outputStream, null);
                outputStream.close();
                // Hide the choose directory button
                chooseDirButton.setVisible(false);
            } catch (Exception e) {
                System.out.println("Error saving properties: " + e.getMessage());
            }
        }
    }

    public String getMusicFolder() {
        return musicFolder;
    }
}