package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

/**
 * @author Huang hao
 */
class LoginModel {
    @Value("id")
    @Path("account")
    lateinit var id: String

    @Value("msg")
    lateinit var msg: String

    @Value("cookie")
    val cookie = ""
}