package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

class CaptchaModel {
    @Value("code")
    var code = 0

    @Value("message")
    val message = ""
}