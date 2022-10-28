package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class UserDetailBean {

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
