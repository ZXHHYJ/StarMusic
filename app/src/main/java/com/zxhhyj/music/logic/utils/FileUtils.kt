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

    fun getFolderSize(folder: File): Long {
        var size: Long = 0

        if (folder.isDirectory) {
            val files = folder.listFiles()

            if (files != null) {
                for (file in files) {
                    size += if (file.isDirectory) {
                        getFolderSize(file)
                    } else {
                        file.length()
                    }
                }
            }
        } else {
            size = folder.length()
        }

        return size
    }

    fun deleteFolder(folder: File): Boolean {
        if (!folder.exists() || !folder.isDirectory) {
            // 文件夹不存在或不是一个有效的文件夹路径
            return false
        }

        val files = folder.listFiles()

        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    // 递归删除子文件夹
                    deleteFolder(file)
                } else {
                    // 删除文件
                    file.delete()
                }
            }
        }

        // 删除空文件夹
        return folder.delete()
    }
}