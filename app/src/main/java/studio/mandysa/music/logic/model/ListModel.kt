package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

class ListModel {

    @Value("coverImgUrl")
    lateinit var coverImgUrl: String

    @Value("updateFrequency")
    lateinit var updateFrequency: String

    @Value("description")
    lateinit var description: String

    @Value("name")
    lateinit var name: String

    @Value("id")
    lateinit var id: String
}