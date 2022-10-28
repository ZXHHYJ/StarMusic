package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value

class ArtistSubBean {
    @Value("picUrl")
    lateinit var picUrl: String

    @Value("name")
    lateinit var nickname: String

    @Value("albumSize")
    lateinit var albumSize: String

    @Value("id")
    lateinit var id: String
}