package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class PhoneLoginModel {

    @Value("code")
    val code = 0

    @Value("id")
    @Path("account")
    lateinit var id: String

    @Value("cookie")
    val cookie: String = ""

    @Value("message")
    val message: String = ""

}