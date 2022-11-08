package studio.mandysa.music.logic.bean

import studio.mandysa.music.service.playmanager.bean.MateSongBean

data class LocalMusicBean(
    override val album: String,
    override val albumId: Long,
    override val artist: String,
    override val artistId: Long,
    override val duration: Long,
    override val data: String,
    override val songName: String,
) : MateSongBean(album, albumId, artist, artistId, duration, data, songName)