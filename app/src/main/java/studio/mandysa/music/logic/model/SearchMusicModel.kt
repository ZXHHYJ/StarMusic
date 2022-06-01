package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.logic.network.MUSIC_URL
import studio.mandysa.music.service.playmanager.model.MateMusic

/**
 * @author Huang hao
 */
class SearchMusicModel :
    MateMusic<SingerModel, AlbumModel> {
    @Value("name")
    private val name = ""

    @Value("id")
    private val id = ""

    @Value("album")
    private val album: AlbumModel? = null

    @Value("artists")
    private val artistsList: List<SingerModel>? = null

    override fun getArtist(): List<SingerModel> {
        return artistsList!!
    }

    override fun getCoverUrl(): String {
        return ""
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
        return album!!
    }

}