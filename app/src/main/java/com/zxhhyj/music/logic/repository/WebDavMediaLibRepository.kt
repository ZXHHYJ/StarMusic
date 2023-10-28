package com.zxhhyj.music.logic.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.kyant.tag.Metadata
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavFile
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.ui.common.POPWindows
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

object WebDavMediaLibRepository {

    var davFileList by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "webdav_song_files",
        initialValue = listOf<WebDavFile>()
    )
        private set

    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "webdav_songs",
        initialValue = listOf<SongBean.WebDav>()
    )
        private set

    var downloadList by mutableStateOf<List<WebDavFile>>(listOf())
        private set

    private fun createSardine(): OkHttpSardine {
        return OkHttpSardine().apply {
            setCredentials(
                SettingRepository.WebDavConfig.username,
                SettingRepository.WebDavConfig.password
            )
        }
    }

    fun download(webDavFile: WebDavFile) {
        if (songs.find { it.webDavFile == webDavFile } != null) {
            return
        }
        downloadList += webDavFile
        POPWindows.postValue("开始下载")
        CoroutineScope(Dispatchers.Default).launch {
            val directory = MainApplication.context.getExternalFilesDir("music")
            val songFile = File(directory, webDavFile.name)
            withContext(Dispatchers.IO) {
                val sardine = createSardine()
                try {
                    val inputStream =
                        sardine.get(webDavFile.downloadPath)
                    FileOutputStream(songFile).use { output ->
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                        }
                        output.flush()
                    }
                    POPWindows.postValue("下载成功")
                } catch (_: Exception) {
                    POPWindows.postValue("下载失败")
                    songFile.delete()
                    return@withContext
                } finally {
                    downloadList -= webDavFile
                }
                val metadata = Metadata.getMetadata(songFile.absolutePath)
                val coverUrl = Metadata.getPicture(songFile.absolutePath)?.let {
                    BitmapFactory.decodeByteArray(it, 0, it.size)
                }?.let {
                    val coverFile = File(directory, webDavFile.eTag)
                    try {
                        val outputStream = BufferedOutputStream(FileOutputStream(coverFile))
                        it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.flush()
                        outputStream.close()
                    } catch (_: Exception) {
                        coverFile.delete()
                        return@let ""
                    }
                    return@let coverFile.absolutePath
                } ?: ""
                songs += SongBean.WebDav(
                    webDavFile,
                    coverUrl = coverUrl,
                    album = SongBean.Album(metadata?.properties?.get("ALBUM")?.first() ?: ""),
                    artist = SongBean.Artist(
                        metadata?.properties?.get("ARTIST")?.first() ?: ""
                    ),
                    duration = metadata?.lengthInMilliseconds ?: 0,
                    data = songFile.absolutePath,
                    dateModified = songFile.lastModified(),
                    songName = metadata?.properties?.get("TITLE")?.first() ?: "",
                    size = songFile.length(),
                    bitrate = metadata?.bitrate,
                    samplingRate = metadata?.sampleRate,
                    lyric = metadata?.properties?.get("LYRICS")?.get(0),
                    startPosition = 0,
                    endPosition = metadata?.lengthInMilliseconds ?: 0
                )
            }
        }
    }

    suspend fun scanMedia() {
        withContext(Dispatchers.IO) {
            val sardine = createSardine()
            fun traverseFolder(path: String): List<WebDavFile> {
                val fileList = mutableListOf<WebDavFile>()
                try {
                    val files = sardine.list(path)
                    for (file in files) {
                        if (file.isDirectory) {
                            fileList.addAll(traverseFolder("$path${file.name}/"))
                        } else if (containsSongExtension(file.name)) {
                            fileList.add(
                                WebDavFile(
                                    file.path,
                                    file.name,
                                    "$path${file.name}",
                                    file.etag
                                )
                            )
                        }
                    }
                } catch (_: Exception) {
                }
                return fileList
            }

            davFileList = traverseFolder(SettingRepository.WebDavConfig.address)
        }
    }

    private fun containsSongExtension(fileName: String): Boolean {
        val songExtensions = listOf(".mp3", ".wav", ".flac", ".m4a")
        for (extension in songExtensions) {
            if (fileName.contains(extension, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

}