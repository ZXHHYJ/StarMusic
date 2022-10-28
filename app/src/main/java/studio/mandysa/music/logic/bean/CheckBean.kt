package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value

class CheckBean {

    @Value("message")
    lateinit var message: String

    @Value("cookie")
    val cookie: String = ""

    @Value("code")
    val code: Int = 0

}