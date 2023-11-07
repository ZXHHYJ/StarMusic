package com.zxhhyj.music.service.webdavmanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kyant.tag.Metadata
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavFile
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.ui.common.POPWindows
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

@OptIn(DelicateCoroutinesApi::class)
object WebDavManager {
    private val downloadTasks = mutableStateMapOf<String, DownloadTask>()

    fun getDownloadState(webDavFile: WebDavFile): State<DownloadState?> {
        return mutableStateOf(downloadTasks[webDavFile.downloadPath]?.downloadState)
    }

    fun download(webDavFile: WebDavFile) {
        val downloadState = DownloadState()
        val downloadJob =
            GlobalScope.launch(context = Dispatchers.IO) {
                try {
                    val sardine = OkHttpSardine().apply {
                        setCredentials(
                            SettingRepository.WebDavConfig.username,
                            SettingRepository.WebDavConfig.password
                        )
                    }
                    val inputStream = sardine[webDavFile.downloadPath]
                    val directory = MainApplication.context.getExternalFilesDir("music")
                    val songFile = File(directory, webDavFile.name)
                    FileOutputStream(songFile).use { output ->
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        var totalBytesRead: Long = 0
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead
                            downloadState.progress =
                                (totalBytesRead.toFloat() / webDavFile.contentLength.toFloat())
                        }
                        output.flush()
                    }
                    inputStream.close()
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
                        }
                        return@let coverFile.absolutePath
                    } ?: ""
                    val songBean = SongBean.WebDav(
                        webDavFile,
                        coverUrl = coverUrl,
                        album = SongBean.Album(metadata?.properties?.get("ALBUM")?.first() ?: ""),
                        artist = SongBean.Artist(
                            metadata?.properties?.get("ARTIST")?.first() ?: ""
                        ),
                        duration = metadata?.lengthInMilliseconds ?: 0,
                        data = songFile.absolutePath,
                        dateModified = System.currentTimeMillis(),
                        songName = metadata?.properties?.get("TITLE")?.first() ?: "",
                        size = songFile.length(),
                        bitrate = metadata?.bitrate,
                        samplingRate = metadata?.sampleRate,
                        lyric = metadata?.properties?.get("LYRICS")?.get(0),
                        startPosition = 0,
                        endPosition = metadata?.lengthInMilliseconds ?: 0
                    )
                    WebDavMediaLibRepository.addSong(songBean)
                    POPWindows.postValue("下载成功")
                } catch (_: Exception) {
                    POPWindows.postValue("下载失败")
                } finally {
                    downloadTasks.remove(webDavFile.downloadPath)
                }
            }
        downloadTasks[webDavFile.downloadPath] =
            DownloadTask(webDavFile, downloadState, downloadJob)
    }
}

data class DownloadTask(
    val webDavFile: WebDavFile,
    val downloadState: DownloadState,
    val job: Job
)

class DownloadState {
    var progress by mutableFloatStateOf(0f)
}