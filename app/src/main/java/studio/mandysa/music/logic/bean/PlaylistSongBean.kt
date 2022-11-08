package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.bean.MetaMusic

class PlaylistSongBean : MetaMusic<SingerBean, AlbumBean> {
    @Value("name")
    private lateinit var name: String

    @Value("id")
    private lateinit var id: String

    @Value("ar")
    private lateinit var artistsList: List<SingerBean>

    @Value("al")
    private lateinit var album: AlbumBean

    override fun getTitle(): String {
        return name
    }

    override fun getCoverUrl(): String {
        return album.coverUrl
    }

    override fun getArtist(): List<SingerBean> {
        return artistsList
    }

    override fun getId(): String {
        return id
    }

    override fun getAlbum(): AlbumBean {
        return album
    }

    override fun toString(): String {
        return name
    }
}
