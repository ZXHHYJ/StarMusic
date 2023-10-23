package com.zxhhyj.music.logic.utils

import android.graphics.Bitmap

object BitmapUtils {
    fun compressBitmap(bitmap: Bitmap): Bitmap {
        val px = 256
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var compressedBitmap = bitmap
        if (originalWidth > px || originalHeight > px) {
            // 计算缩放比例
            val scaleFactor = (originalWidth / px).coerceAtLeast(originalHeight / px)
            val scaledWidth = originalWidth / scaleFactor
            val scaledHeight = originalHeight / scaleFactor
            // 缩放 Bitmap
            compressedBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
        }
        // 将 Bitmap 转换为 RGB_565 格式
        val config = Bitmap.Config.RGB_565
        return compressedBitmap.copy(config, false)
    }
}