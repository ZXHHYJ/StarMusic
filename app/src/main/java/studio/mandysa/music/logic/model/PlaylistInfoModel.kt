package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

/**
 * @author 黄浩
 */
class PlaylistInfoModel {
    @Value("name")
    lateinit var name: String

    @Value("description")
    lateinit var description: String

    @Value("coverImgUrl")
    lateinit var coverImgUrl: String
}