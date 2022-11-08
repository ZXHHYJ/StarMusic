package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.bean.MetaMusic

/**
 * @author 黄浩
 */
class MusicBean(
    @Value("id") private val id: String,
    @Value("name") private val name: String,
    @Value("al") private val album: AlbumBean,
    @Value("ar") private val artists: List<SingerBean>
) : MetaMusic<SingerBean, AlbumBean> {

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