package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

/**
 * @author Huang hao
 */
class PlaylistInfoModel {
    @Value("name")
    val name = ""

    @Value("description")
    val description = ""

    @Value("coverImgUrl")
    val coverImgUrl = ""

    @Value("trackIds")
    val songList: List<SongList>? = null

    class SongList {
        @Value("id")
        private val id = ""

        override fun toString(): String {
            return id
        }
    }
}