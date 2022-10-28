package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaMusic
import java.io.Serializable

/**
 * @author 黄浩
 */
class RecommendSongBean : MetaMusic<SingerBean, AlbumBean>, Serializable {
    @Value("name")
    private lateinit var name: String

    @Value("id")
    private lateinit var id: String

    @Value("ar")
    private lateinit var artistsList: List<SingerBean>

    @Value("al")
    private lateinit var album: AlbumBean

    /*@Value("reason")
    val reason = ""*/

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
}
