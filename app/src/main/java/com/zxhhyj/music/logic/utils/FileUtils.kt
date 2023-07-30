package com.zxhhyj.music.logic.utils

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

object FileUtils {

    /**
     * 从指定路径的文件中读取文件内容，并返回字符串
     */
    fun readFromFile(filePath: String): String {
        val stringBuilder = StringBuilder()
        val inputStream = File(filePath).inputStream()
        val bufferedReader =
            BufferedReader(InputStreamReader(inputStream, Charset.forName("GB2312")))
        bufferedReader.use {
            it.forEachLine { line ->
                stringBuilder.append(line)
                stringBuilder.append(System.lineSeparator())
            }
        }
        return stringBuilder.toString()
    }
}