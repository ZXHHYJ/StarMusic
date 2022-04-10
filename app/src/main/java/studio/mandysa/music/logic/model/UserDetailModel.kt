package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class UserDetailModel {

    @Path("profile")
    @Value("follows")
    val follows = 0

    @Path("profile")
    @Value("followeds")
    val fans = 0

    @Path("profile")
    @Value("vipType")
    val vipType = 0
}
