package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.logic.network.MUSIC_URL
import studio.mandysa.music.service.playmanager.model.MusicModel

/**
 * @author Huang hao
 */
class SearchMusicModelModel : MusicModel<SingerModel, AlbumModel> {
    @Value("name")
    private lateinit var name: String

    @Value("id")
    private lateinit var id: String

    @Value("album")
    private lateinit var album: AlbumModel

    @Value("artists")
    private lateinit var artistsList: List<SingerModel>

    override fun getArtist(): List<SingerModel> {
        return artistsList
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
        return album
    }

}