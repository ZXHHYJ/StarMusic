package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaMusic

/**
 * @author 黄浩
 */
class MusicBean : MetaMusic<SingerBean, AlbumBean> {
    @Value("id")
    private lateinit var id: String

    @Value("name")
    private lateinit var name: String

    @Value("al")
    private lateinit var album: AlbumBean

    @Value("ar")
    private lateinit var artists: List<SingerBean>

    override fun getArtist(): List<SingerBean> {
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

    override fun getAlbum(): AlbumBean {
        return album
    }
}