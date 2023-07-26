package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.config.DataSaverUtils

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
        if (!playlist.any { it.name == name }) {
            val model = PlayListModel()
            playlist = playlist.toMutableList().apply {
                add(model)
            }
            model.rename(name)
        }
    }

    /**
     * 删除歌单
     */
    fun remove(model: PlayListModel) {
        DataSaverUtils.remove(model.nameKey)
        DataSaverUtils.remove(model.songsKey)
        playlist = playlist.toMutableList().apply {
            remove(model)
        }
    }

}