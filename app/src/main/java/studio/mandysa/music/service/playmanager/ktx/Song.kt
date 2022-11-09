package studio.mandysa.music.service.playmanager.ktx

import studio.mandysa.music.service.playmanager.bean.Song

private const val EMPTY_STRING = ""

val Song.coverUrl: String
    get() = when (this) {
        is Song.LocalBean -> "content://media/external/audio/albumart/${this.albumId}"
        is Song.NetworkBean -> this.coverUrl
    }

val Song.title: String
    get() = when (this) {
        is Song.LocalBean -> this.songName
        is Song.NetworkBean -> this.title
    }

val Song.artist: Array<Song.NetworkBean.Artist>
    get() = when (this) {
        is Song.LocalBean -> arrayOf(Song.NetworkBean.Artist(this.artistId.toString(), this.artist))
        is Song.NetworkBean -> this.artist
    }

val Song.album: Song.NetworkBean.Album
    get() = when (this) {
        is Song.LocalBean -> Song.NetworkBean.Album(
            this.albumId.toString(),
            this.coverUrl,
            this.album,
            EMPTY_STRING
        )
        is Song.NetworkBean -> this.album
    }
