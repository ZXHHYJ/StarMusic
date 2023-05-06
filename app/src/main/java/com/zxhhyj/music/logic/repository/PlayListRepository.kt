package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.service.playmanager.bean.SongBean

object PlayListRepository {

    var playlist by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_playlist",
        initialValue = listOf<PlayListModel>()
    )
        private set

    /**
     * 创建歌单
     */
    fun create(name: String) {
        playlist = playlist.toMutableList().also { it ->
            it.forEach {
                if (it.name == name) {
                    return
                }
            }
            it.add(PlayListModel(name, listOf()))
        }
    }

    /**
     * 往歌单中添加歌曲
     */
    fun add(index: Int, vararg songs: SongBean) {
        playlist = playlist.toMutableList().also {
            val list = it[index].songs.toMutableList()
            songs.forEach { song ->
                list.add(song)
            }
            it[index] = it[index].copy(songs = list)
        }
    }

    /**
     * 从歌单中删除歌曲
     */
    fun remove(index: Int, vararg songs: SongBean) {
        playlist = playlist.toMutableList().also {
            val list = it[index].songs.toMutableList()
            songs.forEach { song ->
                list.remove(song)
            }
            it[index] = it[index].copy(songs = list)
        }
    }

    /**
     * 给歌单重命名
     */
    fun rename(index: Int, newName: String) {
        playlist = playlist.toMutableList().also {
            it[index] = it[index].copy(name = newName)
        }
    }

    /**
     * 删除某个歌单
     */
    fun delete(index: Int) {
        playlist = playlist.toMutableList().also {
            it.removeAt(index)
        }
    }
}