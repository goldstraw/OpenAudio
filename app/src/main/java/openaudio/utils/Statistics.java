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
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;


class ListenInstance {
    private Song song;
    private double secondsElapsed;

    public ListenInstance(Song song, double secondsElapsed) {
        this.song = song;
        this.secondsElapsed = secondsElapsed;
    }
}

public class Statistics {

    private static Statistics instance = null;
    private File statsFile = null;
    private List<ListenInstance> listenInstances = null;

    private Statistics() {
        String musicFolder = Settings.getInstance().getMusicFolder();
        new File(musicFolder + "/openaudio.statistics").mkdirs();
        String statsFileName = String.valueOf(System.currentTimeMillis());
        statsFile = new File(musicFolder + "/openaudio.statistics/" + statsFileName + ".json");
    }

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    public void writeStats(Song song, double secondsElapsed) {
        ListenInstance listenInstance = new ListenInstance(song, secondsElapsed);
        Gson gson = new Gson();
        // Load existing stats
        if (listenInstances == null) {
            listenInstances = new ArrayList<ListenInstance>();
        }
        listenInstances.add(listenInstance);
        String json = gson.toJson(listenInstances);
        // Overwrite stats file
        try {
            PrintWriter writer = new PrintWriter(statsFile);
            writer.print(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}