package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value

class CaptchaBean {
    @Value("code")
    var code = 0

    @Value("message")
    val message = ""
}