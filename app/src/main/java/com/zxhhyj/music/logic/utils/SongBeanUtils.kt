package com.zxhhyj.music.logic.utils

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.kyant.taglib.TagLib
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavSource
import com.zxhhyj.music.logic.config.coverFilesDir
import com.zxhhyj.music.logic.repository.SettingRepository
import java.io.File
import java.io.FileOutputStream

private fun createSongBeanLocal(data: String, id: Long?): SongBean.Local? {
    val songFile = File(data)
    val fd = ParcelFileDescriptor.open(songFile, ParcelFileDescriptor.MODE_READ_ONLY)
    val metadata =
        TagLib.getMetadata(fd.dup().detachFd(), String(), readLyrics = true) ?: return null
    val songName =
        metadata.propertyMap["TITLE"]?.getOrNull(0)?.trim()
            ?: songFile.name.substringBeforeLast(".")
    val album = metadata.propertyMap["ALBUM"]?.getOrNull(0)?.trim()
    val artist = metadata.propertyMap["ARTIST"]?.getOrNull(0)?.trim()
    val lyric = metadata.propertyMap["LYRICS"]?.getOrNull(0)?.trim()
        ?: runCatching {
            if (SettingRepository.EnableReadExternalLyrics) {
                File("${songFile.path.substringBeforeLast(".")}.lrc").readText()
            } else {
                null
            }
        }.getOrNull()
    val duration = metadata.audioProperties.length.toLong()
    val bitrate = metadata.audioProperties.bitrate
    val sampleRate = metadata.audioProperties.sampleRate
    val coverFile =
        TagLib.getPictures(fd.dup().detachFd())?.getOrNull(0)?.data?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }?.let {
            runCatching {
                val file = File(coverFilesDir, songName)
                val fos = FileOutputStream(file)
                it.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                file
            }.getOrNull()
        }
    fd.close()
    return SongBean.Local(
        coverUrl = coverFile?.path,
        albumName = album,
        artistName = artist,
        duration = duration,
        data = data,
        dateModified = songFile.lastModified(),
        songName = songName,
        size = songFile.length(),
        id = id,
        bitrate = bitrate,
        samplingRate = sampleRate,
        lyric = lyric,
    )
}

fun File.toSongBeanLocal(): SongBean.Local? {
    return createSongBeanLocal(path, null)
}

/**
 * 读取[File]信息来创建一个[SongBean.WebDav]
 * @param url 在WebDav中的网路地址
 */
fun File.toSongBeanWebDav(source: WebDavSource, url: String): SongBean.WebDav? {
    val localSongBean = toSongBeanLocal()
    localSongBean?.let {
        return SongBean.WebDav(
            source,
            localSongBean.coverUrl,
            localSongBean.albumName,
            localSongBean.artistName,
            localSongBean.duration,
            url,
            localSongBean.dateModified,
            localSongBean.songName,
            localSongBean.size,
            localSongBean.bitrate,
            localSongBean.samplingRate,
            localSongBean.lyric
        )
    }
    return null
}

fun Cursor.toSongBeanLocal(): SongBean.Local? {
    val dataIndex = getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
    val idIndex = getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val data = getString(dataIndex)
    val id = getLong(idIndex)
    return createSongBeanLocal(data, id)
}