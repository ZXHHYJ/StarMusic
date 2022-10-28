package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import java.io.Serializable

/**
 * @author 黄浩
 */
class PlaylistInfoBean : Serializable {
    @Value("name")
    lateinit var name: String

    @Value("description")
    lateinit var description: String

    @Value("coverImgUrl")
    lateinit var coverImgUrl: String
}