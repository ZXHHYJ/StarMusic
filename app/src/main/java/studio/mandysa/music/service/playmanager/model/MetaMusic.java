package studio.mandysa.music.service.playmanager.model;

import java.util.List;

/**
 * @author Huang hao
 */
public interface MetaMusic<T extends MetaArtist, E extends MetaAlbum> {
    String getTitle();

    String getId();

    String getCoverUrl();

    String getUrl();

    List<T> getArtist();

    E getAlbum();
}
