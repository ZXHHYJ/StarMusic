package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import java.io.Serializable

/**
 * @author 黄浩
 */
class PlaylistInfoModel : Serializable {
    @Value("name")
    lateinit var name: String

    @Value("description")
    lateinit var description: String

    @Value("coverImgUrl")
    lateinit var coverImgUrl: String
}