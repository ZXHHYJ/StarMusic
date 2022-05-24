package studio.mandysa.music.logic.user

import simon.tuke.Tuke

fun cookie(): String {
    return Tuke.tukeGet("cookie_key", "")
}

fun userId(): String {
    return Tuke.tukeGet("userid_key", "")
}