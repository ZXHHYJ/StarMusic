package com.zxhhyj.music.logic.ktx

// 将毫秒数转换为分钟:秒钟格式的字符串（Int类型）
fun Int.toTime(): String {
    // 将毫秒数转换为秒数
    val s: Int = this / 1000
    // 将秒数转换为分钟:秒钟格式的字符串
    return (s / 60).toString() + ":" + s % 60
}

// 将毫秒数转换为分钟:秒钟格式的字符串（Long类型）
fun Long.toTime(): String {
    // 将毫秒数转换为秒数
    val s = this / 1000
    // 将秒数转换为分钟:秒钟格式的字符串
    return (s / 60).toString() + ":" + s % 60
}