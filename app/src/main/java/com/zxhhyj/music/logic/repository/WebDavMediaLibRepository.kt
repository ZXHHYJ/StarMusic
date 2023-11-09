package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavFile
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.logic.utils.SardineUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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

    fun addSong(song: SongBean.WebDav) {
        songs += song
    }

    fun removeSong(song: SongBean.WebDav) {
        songs -= song
    }

    suspend fun scanMedia() {
        withContext(Dispatchers.IO) {
            val sardine = SardineUtils.createSardine()
            try {
                sardine.list(SettingRepository.WebDavConfig.address)
                //先进行一次连通性测试
            } catch (e: Exception) {
                throw e
            }
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
                                    path + file.name,
                                    file.etag,
                                    file.contentLength
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

    fun clear() {
        davFileList = emptyList()
        for (song in songs) {
            val file = File(song.data)
            file.delete()
        }
        songs = emptyList()
    }

}