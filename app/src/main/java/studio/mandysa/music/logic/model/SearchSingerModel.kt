package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

/**
 * @author Huang hao
 */
class SearchSingerModel {
    @Value("id")
    lateinit var id: String

    @Value("name")
    lateinit var name: String

    @Value("picUrl")
    lateinit var picUrl: String

    @Value("followed")
    var followed = false

    @Value("albumSize")
    var albumSize = 0
}