package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.logic.network.MUSIC_URL
import studio.mandysa.music.service.playmanager.model.MetaMusic

/**
 * @author Huang hao
 */
class SearchSongModel : MetaMusic<SingerModel, AlbumModel> {
    @Value("name")
    private lateinit var name: String

    @Value("id")
    private lateinit var id: String

    @Value("al")
    private lateinit var album: AlbumModel

    @Value("ar")
    private lateinit var artists: List<SingerModel>

    override fun getArtist(): List<SingerModel> {
        return artists
    }

    override fun getCoverUrl(): String {
        return album.coverUrl
    }

    override fun getId(): String {
        return id
    }

    override fun getTitle(): String {
        return name
    }

    override fun getUrl(): String {
        return MUSIC_URL + id
    }

    override fun toString(): String {
        return id
    }

    override fun getAlbum(): AlbumModel {
        return album
    }

}