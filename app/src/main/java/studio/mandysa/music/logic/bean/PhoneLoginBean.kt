package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class PhoneLoginBean {

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