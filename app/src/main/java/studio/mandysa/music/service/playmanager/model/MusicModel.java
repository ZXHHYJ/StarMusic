package studio.mandysa.music.service.playmanager.model;

import java.util.List;

/**
 * @author Huang hao
 */
public interface MusicModel<T extends ArtistModel, E extends AlbumModel> {
    String getTitle();

    String getId();

    String getCoverUrl();

    String getUrl();

    List<T> getArtist();

    E getAlbum();
}
