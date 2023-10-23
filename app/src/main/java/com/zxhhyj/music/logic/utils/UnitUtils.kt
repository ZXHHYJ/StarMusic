package com.zxhhyj.music.logic.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 将毫秒数转换为分钟:秒钟格式的字符串（Int类型）
fun Int.toTimeString(): String {
    // 将毫秒数转换为秒数
    val s: Int = this / 1000
    // 将秒数转换为分钟:秒钟格式的字符串
    return (s / 60).toString() + ":" + s % 60
}

// 将毫秒数转换为分钟:秒钟格式的字符串（Long类型）
fun Long.toTimeString(): String {
    // 将毫秒数转换为秒数
    val s = this / 1000
    // 将秒数转换为分钟:秒钟格式的字符串
    return (s / 60).toString() + ":" + s % 60
}

fun String.toMillis(): Long {
    val timeParts = this.split(":")
    val minutes = timeParts[0].toLong()
    val seconds = timeParts[1].toLong()
    val milliseconds = timeParts[2].padEnd(3, '0').substring(0, 3).toLong()
    return ((minutes * 60 + seconds) * 1000 + milliseconds)
}

fun timestampToString(timestamp: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(Date(timestamp * 1000))
}