package openaudio.utils;

import openaudio.utils.Settings;
import openaudio.models.Song;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class Statistics {

    private static Statistics instance = null;
    private File statsFile = null;

    private Statistics() {
        String musicFolder = Settings.getInstance().getMusicFolder();
        new File(musicFolder + "/openaudio.statistics").mkdirs();
        String statsFileName = String.valueOf(System.currentTimeMillis());
        statsFile = new File(musicFolder + "/openaudio.statistics/" + statsFileName + ".stats");
    }

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    public void writeStats(Song song, double secondsElapsed) {
        try {
            FileWriter fileWriter = new FileWriter(statsFile, true);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.println("T: " + song.getTitle());
            writer.println("A: " + song.getArtist());
            writer.println("R: " + song.getAlbum());
            writer.println("Y: " + song.getYear());
            writer.println("G: " + song.getGenre());
            writer.println("N: " + song.getTrack());
            writer.println("D: " + song.getDuration());
            writer.println("S: " + secondsElapsed);

            writer.close();
        } catch (java.io.IOException e) {
            System.out.println("Failed to save statistics");
        }
    }

}