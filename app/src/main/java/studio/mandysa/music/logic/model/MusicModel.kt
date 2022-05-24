package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.logic.network.Url
import studio.mandysa.music.service.playmanager.model.MateMusic

/**
 * @author Huang hao
 */
class MusicModel : MateMusic<SingerModel, AlbumModel> {
    @Value("id")
    private val id = ""

    @Value("name")
    private val name = ""

    @Value("al")
    private val album: AlbumModel? = null

    @Value("al")
    private val albumList: AlbumModel? = null

    @Value("ar")
    private val artistsList: List<SingerModel>? = null

    override fun getArtist(): List<SingerModel> {
        return artistsList!!
    }

    override fun getId(): String {
        return id
    }

    override fun getCoverUrl(): String {
        return album!!.coverUrl
    }

    override fun getTitle(): String {
        return name
    }

    override fun getUrl(): String {
        return Url.MUSIC_URL + id
    }

    override fun getAlbum(): AlbumModel {
        return album!!
    }
}