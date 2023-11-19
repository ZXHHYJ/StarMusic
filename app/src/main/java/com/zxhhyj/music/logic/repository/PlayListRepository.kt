package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.bean.PlayListSongBean
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.config.DataSaverUtils
import java.util.UUID

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
     */
    fun PlayListBean.addSong(songBean: SongBean) {
        playList += this.copy(songs = this.songs.toMutableList().apply {
            val songToAdd =
                PlayListSongBean(songBean.songName, songBean.artist.name, songBean.album.name)
            add(songToAdd)
        })
        playList -= this
    }

    fun PlayListBean.removeSong(songBean: SongBean) {
        playList += this.copy(songs = this.songs.toMutableList().apply {
            val songToRemove = songs.find { song ->
                songBean.songName == song.songName &&
                        songBean.artist.name == song.artistName &&
                        songBean.album.name == song.albumName
            }
            remove(songToRemove)
        })
        playList -= this
    }

    /**
     * 重命名播放列表
     */
    fun PlayListBean.rename(name: String) {
        playList += this.copy(name = name)
        playList -= this
    }

    /**
     * 创建新的播放列表
     */
    fun create(name: String) {
        if (!playList.any { it.name == name }) {
            playList += PlayListBean(UUID.randomUUID().toString(), name, listOf())
        }
    }

    /**
     * 移除播放列表
     */
    fun remove(model: PlayListBean) {
        playList -= model
    }
}