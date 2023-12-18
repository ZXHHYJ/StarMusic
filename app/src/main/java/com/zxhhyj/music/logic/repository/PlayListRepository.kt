package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.bean.PlayListSongBean
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.config.DataSaverUtils
import java.util.UUID

/**
 * 播放列表的仓库类，用于管理播放列表的数据。
 */
object PlayListRepository {

    /**
     * 新版播放列表
     */
    var playList by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_playlist_v2",
        initialValue = listOf<PlayListBean>()
    )
        private set

    /**
     * 向播放列表添加歌曲
     *
     * @param songBean 要添加的歌曲
     */
    fun PlayListBean.addSong(songBean: SongBean) {
        playList += copy(songs = songs.toMutableList().apply {
            add(PlayListSongBean(songBean.data))
        })
        playList -= this
    }

    /**
     * 从播放列表移除歌曲
     *
     * @param songBean 要移除的歌曲
     */
    fun PlayListBean.removeSong(songBean: SongBean) {
        playList += copy(songs = songs.toMutableList().apply {
            val songToRemove = songs.find { song ->
                songBean.data == song.data
            }
            remove(songToRemove)
        })
        playList -= this
    }

    /**
     * 重命名播放列表
     *
     * @param name 新的播放列表名称
     */
    fun PlayListBean.rename(name: String) {
        playList += this.copy(name = name)
        playList -= this
    }

    /**
     * 创建新的播放列表
     *
     * @param name 播放列表名称
     */
    fun create(name: String) {
        if (!playList.any { it.name == name }) {
            playList += PlayListBean(UUID.randomUUID().toString(), name, listOf())
        }
    }

    /**
     * 移除播放列表
     *
     * @param model 要移除的播放列表
     */
    fun remove(model: PlayListBean) {
        playList -= model
    }
}