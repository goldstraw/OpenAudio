package openaudio.models;

import java.io.File;
import openaudio.utils.Settings;
import javafx.scene.image.Image;
import java.util.List;

public interface SongCollection {
    List<Song> getSongs();
    String getName();
    String getArtist();
    Image getCoverImage();
}