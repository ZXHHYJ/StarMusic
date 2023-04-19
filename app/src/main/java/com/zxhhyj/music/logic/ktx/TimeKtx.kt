package com.zxhhyj.music.logic.ktx

fun Int.toTime(): String {
    val s: Int = this / 1000
    return (s / 60).toString() + ":" + s % 60
}

fun Long.toTime(): String {
    val s = this / 1000
    return (s / 60).toString() + ":" + s % 60
}