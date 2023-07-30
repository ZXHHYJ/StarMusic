package com.zxhhyj.music.service.playmanager.impl

import com.zxhhyj.music.logic.bean.SongBean

interface PlayManagerController {
    // 播放指定列表中的指定位置的歌曲
    fun play(list: List<SongBean>, index: Int)

    // 将播放进度跳转到指定位置
    fun seekTo(position: Int)

    // 跳转到上一首歌曲
    fun skipToPrevious()

    // 跳转到下一首歌曲
    fun skipToNext()

    // 播放当前歌曲
    fun play()

    // 暂停当前歌曲
    fun pause()

    // 停止播放
    fun stop()

    // 将歌曲添加到播放列表中下一首播放
    fun addNextPlay(song: SongBean)

    // 删除指定歌曲
    fun removeSong(song: SongBean)

    // 清空播放列表并停止播放
    fun clearPlayList()
}