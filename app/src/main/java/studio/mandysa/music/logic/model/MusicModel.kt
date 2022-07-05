package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaMusic

/**
 * @author 黄浩
 */
class MusicModel : MetaMusic<SingerModel, AlbumModel> {
    @Value("id")
    private lateinit var id: String

    @Value("name")
    private lateinit var name: String

    @Value("al")
    private lateinit var album: AlbumModel

    @Value("ar")
    private lateinit var artists: List<SingerModel>

    override fun getArtist(): List<SingerModel> {
        return artists
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

    override fun getAlbum(): AlbumModel {
        return album
    }
}