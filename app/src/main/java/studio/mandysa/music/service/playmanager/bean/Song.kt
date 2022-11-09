package studio.mandysa.music.service.playmanager.bean

sealed interface Song {
    data class LocalBean(
        val album: String,
        val albumId: Long,
        val artist: String,
        val artistId: Long,
        val duration: Long,
        val data: String,
        val songName: String
    ) : Song

    open class NetworkBean(
        open val id: String,
        open val title: String,
        open val coverUrl: String,
        open val artist: Array<Artist>,
        open val album: Album
    ) : Song {

        open class Artist(open val id: String, open val name: String)

        open class Album(
            open val id: String,
            open val coverUrl: String,
            open val name: String,
            open val publishTime: String
        )
    }

}