package studio.mandysa.music.service.playmanager.bean

sealed interface SongBean {
    data class Local(
        val album: Album,
        val artist: Artist,
        val duration: Long,
        val data: String,
        val songName: String
    ) : SongBean {

        data class Artist(val id: String, val name: String)

        data class Album(val id: String, val name: String)
    }

    open class Network(
        open val id: String,
        open val title: String,
        open val coverUrl: String,
        open val artist: Array<Artist>,
        open val album: Album
    ) : SongBean {

        open class Artist(open val id: String, open val name: String)

        open class Album(
            open val id: String,
            open val coverUrl: String,
            open val name: String,
            open val publishTime: String
        )
    }

}