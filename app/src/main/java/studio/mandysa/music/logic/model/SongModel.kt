package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.logic.network.MUSIC_URL
import studio.mandysa.music.service.playmanager.model.MusicModel

/**
 * @author Huang hao
 */
class SongModel : MusicModel<SingerModel, AlbumModel> {
    @Value("id")
    private lateinit var id: String

    @Value("name")
    private lateinit var name: String

    @Value("al")
    private lateinit var album: AlbumModel

    @Value("al")
    private lateinit var albumList: AlbumModel

    @Value("ar")
    private lateinit var artistsList: List<SingerModel>

    override fun getArtist(): List<SingerModel> {
        return artistsList
    }

    override fun getId(): String {
        return id
    }

    override fun getCoverUrl(): String {
        return album.coverUrl
    }

    override fun getTitle(): String {
        return name
    }

    override fun getUrl(): String {
        return MUSIC_URL + id
    }

    override fun getAlbum(): AlbumModel {
        return album
    }
}