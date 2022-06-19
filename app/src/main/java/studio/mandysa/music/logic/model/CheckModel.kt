package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

class CheckModel {

    @Value("message")
    lateinit var message: String

    @Value("cookie")
    val cookie: String = ""

    @Value("code")
    val code: Int = 0

}