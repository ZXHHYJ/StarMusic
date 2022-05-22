package studio.mandysa.music.logic.cookie

import simon.tuke.Tuke

fun cookie(): String {
    return Tuke.tukeGet("cookie_key")
}