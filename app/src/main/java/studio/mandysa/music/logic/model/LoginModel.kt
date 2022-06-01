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

    @Value("cookie")
    val cookie = ""

    @Value("code")
    val code = 0

    @Value("msg")
    lateinit var msg: String
}