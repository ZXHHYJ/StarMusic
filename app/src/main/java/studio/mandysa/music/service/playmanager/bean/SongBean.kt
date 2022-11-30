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
    ) : SongBean {
        data class Artist(val id: Long, val name: String, val songs: List<Local>) {
            override fun toString(): String {
                return id.toString()
            }
        }

        data class Album(
            val albumId: Long,
            val album: String,
            val artistId: Long,
            val artist: String,
            val songs: List<Local>
        ) {
            override fun toString(): String {
                return albumId.toString()
            }
        }
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