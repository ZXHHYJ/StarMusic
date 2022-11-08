package studio.mandysa.music.service.playmanager.bean

sealed interface SongBean {
    data class Local(
        val album: String,
        val albumId: Long,
        val artist: String,
        val artistId: Long,
        val duration: Long,
        val data: String,
        val songName: String
    ) : SongBean

    open class NeteaseClound(

    ) : SongBean
}