package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class BannerModel {
    @Value("pic")
    var pic = ""

    @Path("song/al")
    @Value("id")
    val albumId = 0

    @Value("encodeId")
    val encodeId = ""

    @Value("targetType")
    val targetType = 0

    @Value("typeTitle")
    var typeTitle = ""

    @Value("url")
    val url = ""

}