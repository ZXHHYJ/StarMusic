package studio.mandysa.music.logic.bean

import studio.mandysa.music.service.playmanager.model.MetaAlbum
import studio.mandysa.music.service.playmanager.model.MetaArtist
import studio.mandysa.music.service.playmanager.model.MetaMusic
import java.io.Serializable

/**
 * @author 黄浩
 */
class SerialSongBean(
    private val title: String,
    private val id: String,
    private val coverUrl: String,
    private val artists: MutableList<SerialArtist>,
    private val album: SerialAlbum
) : MetaMusic<SerialSongBean.SerialArtist, SerialSongBean.SerialAlbum>,
    Serializable {

    override fun getTitle(): String = title

    override fun getId(): String = id

    override fun getCoverUrl(): String = coverUrl

    override fun getArtist(): MutableList<SerialArtist> = artists

    override fun getAlbum(): SerialAlbum = album

    class SerialArtist(private val id: String, private val name: String) : MetaArtist,
        Serializable {

        override fun getId(): String = id

        override fun getName(): String = name

    }

    class SerialAlbum(
        private val id: String,
        private val coverUrl: String,
        private val name: String,
        private val publishTime: String
    ) : MetaAlbum, Serializable {
        override fun getId(): String = id

        override fun getCoverUrl(): String = coverUrl

        override fun getName(): String = name

        override fun getPublishTime(): String = publishTime

    }
}