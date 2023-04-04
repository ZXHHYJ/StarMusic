package studio.mandysa.music.logic.helper

fun Long.toTime(): String {
    val s = this / 1000
    return (s / 60).toString() + ":" + s % 60
}

fun Int.toTime(): String {
    val s: Int = this / 1000
    return (s / 60).toString() + ":" + s % 60
}