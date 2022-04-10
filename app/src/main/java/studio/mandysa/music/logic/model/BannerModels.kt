package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class BannerModels {

    @Value("banners")
    val list: List<BannerModel>? = null

    class BannerModel {
        @Value("pic")
        val pic = ""

        @Path("song/al")
        @Value("id")
        val albumId = 0

        @Value("encodeId")
        val encodeId = ""

        @Value("targetType")
        val targetType = 0

        @Value("typeTitle")
        val typeTitle = ""

        @Value("url")
        val url = ""
    }
}