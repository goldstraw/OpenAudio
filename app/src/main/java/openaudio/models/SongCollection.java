package openaudio.models;

import java.io.File;
import openaudio.utils.Settings;
import openaudio.models.Song;
import javafx.scene.image.Image;
import java.util.List;

public interface SongCollection {
    List<Song> getSongs();
    List<Song> getRemainingSongs(Song song);
    String getName();
    String getArtist();
    Image getCoverImage();
}