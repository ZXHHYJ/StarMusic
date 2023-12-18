package com.zxhhyj.music.logic.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils

/**
 * 从Uri中获取真实路径
 *
 * @param context 上下文
 * @param uri Uri对象
 * @return Uri对应的真实路径
 */
fun getRealPathFromUri(context: Context, uri: Uri): String? {
    var filePath: String? = ""
    val scheme = uri.scheme
    if (scheme == null) {
        filePath = uri.path
    } else if (ContentResolver.SCHEME_FILE == scheme) {
        filePath = uri.path
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
        }
        if (TextUtils.isEmpty(filePath)) {
            filePath = getFilePathForNonMediaUri(context, uri)
        }
    }
    return filePath
}

/**
 * 从非媒体文件中获取文件路径
 *
 * @param context 上下文
 * @param uri Uri对象
 * @return Uri对应的文件路径
 */
private fun getFilePathForNonMediaUri(context: Context, uri: Uri): String {
    var filePath = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow("_data")
            filePath = it.getString(columnIndex)
        }
    }
    return filePath
}