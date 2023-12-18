package com.zxhhyj.music.logic.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 将毫秒数转换为分钟:秒钟格式的字符串（Int类型）
 *
 * @return 转换后的分钟:秒钟格式的字符串
 */
fun Int.toTimeString(): String {
    // 将毫秒数转换为秒数
    val s: Int = this / 1000
    // 将秒数转换为分钟:秒钟格式的字符串
    return (s / 60).toString() + ":" + s % 60
}

/**
 * 将毫秒数转换为分钟:秒钟格式的字符串（Long类型）
 *
 * @return 转换后的分钟:秒钟格式的字符串
 */
fun Long.toTimeString(): String {
    // 将毫秒数转换为秒数
    val s = this / 1000
    // 将秒数转换为分钟:秒钟格式的字符串
    return (s / 60).toString() + ":" + s % 60
}

/**
 * 将分钟:秒钟格式的字符串转换为毫秒数
 *
 * @return 转换后的毫秒数
 */
fun String.toMillis(): Long {
    val timeParts = this.split(":")
    val minutes = timeParts[0].toLong()
    val seconds = timeParts[1].toLong()
    val milliseconds = timeParts[2].padEnd(3, '0').substring(0, 3).toLong()
    return ((minutes * 60 + seconds) * 1000 + milliseconds)
}

/**
 * 将时间戳转换为字符串格式（yyyy-MM-dd HH:mm:ss）
 *
 * @param timestamp 时间戳（单位：秒）
 * @return 格式化后的时间字符串
 */
fun timestampToString(timestamp: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(Date(timestamp))
}