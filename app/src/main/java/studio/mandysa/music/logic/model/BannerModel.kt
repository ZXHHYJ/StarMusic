package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class BannerModel {
    @Value("pic")
    lateinit var pic: String

    @Path("song/al")
    @Value("id")
    lateinit var albumId: String

    @Value("encodeId")
    lateinit var encodeId: String

    @Value("targetType")
    val targetType = 0

    @Value("typeTitle")
    lateinit var typeTitle: String

    @Value("url")
    lateinit var url: String

}