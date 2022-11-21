package studio.mandysa.music.service.playmanager.ktx

import studio.mandysa.music.service.playmanager.bean.SongBean

private const val EMPTY_STRING = ""

val SongBean.coverUrl: String
    get() = when (this) {
        is SongBean.Local -> "content://media/external/audio/albumart/${this.albumId}"
        is SongBean.Network -> this.coverUrl
    }

val SongBean.title: String
    get() = when (this) {
        is SongBean.Local -> this.songName
        is SongBean.Network -> this.title
    }

val SongBean.artist: Array<SongBean.Network.Artist>
    get() = when (this) {
        is SongBean.Local -> arrayOf(SongBean.Network.Artist(this.artistId.toString(), this.artist))
        is SongBean.Network -> this.artist
    }

val SongBean.album: SongBean.Network.Album
    get() = when (this) {
        is SongBean.Local -> SongBean.Network.Album(
            this.albumId.toString(),
            this.coverUrl,
            this.album,
            EMPTY_STRING
        )
        is SongBean.Network -> this.album
    }
