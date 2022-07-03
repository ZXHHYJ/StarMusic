package studio.mandysa.music.service.playmanager.model;

import java.util.List;

/**
 * @author 黄浩
 */
public interface MetaMusic<T extends MetaArtist, E extends MetaAlbum> {
    String getTitle();

    String getId();

    String getCoverUrl();

    String getUrl();

    List<T> getArtist();

    E getAlbum();
}
