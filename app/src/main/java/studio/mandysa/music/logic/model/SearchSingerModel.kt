package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

/**
 * @author 黄浩
 */
class SearchSingerModel {
    @Value("id")
    lateinit var id: String

    @Value("name")
    lateinit var name: String

    @Value("picUrl")
    lateinit var picUrl: String

    @Value("albumSize")
    val albumSize = 0
}