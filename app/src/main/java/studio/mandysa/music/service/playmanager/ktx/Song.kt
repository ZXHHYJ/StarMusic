package studio.mandysa.music.service.playmanager.ktx

import studio.mandysa.music.service.playmanager.bean.SongBean

private const val EMPTY_STRING = ""

/**
 * @author 黄浩
 */

val SongBean.coverUrl: String
    get() = when (this) {
        is SongBean.Local -> "content://media/external/audio/albumart/${this.album.id}"
        is SongBean.Network -> this.coverUrl
    }

val SongBean.title: String
    get() = when (this) {
        is SongBean.Local -> this.songName
        is SongBean.Network -> this.title
    }

val SongBean.artist: Array<SongBean.Network.Artist>
    get() = when (this) {
        is SongBean.Local -> arrayOf(SongBean.Network.Artist(this.artist.id, this.artist.name))
        is SongBean.Network -> this.artist
    }

val SongBean.album: SongBean.Network.Album
    get() = when (this) {
        is SongBean.Local -> SongBean.Network.Album(
            this.album.id,
            this.coverUrl,
            this.album.name,
            EMPTY_STRING
        )
        is SongBean.Network -> this.album
    }
