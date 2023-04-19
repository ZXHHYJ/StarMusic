package com.zxhhyj.music.service.playmanager.impl

import com.zxhhyj.music.service.playmanager.bean.SongBean

interface PlayManagerController {
    fun play(list: List<SongBean>, index: Int)

    fun seekTo(position: Int)

    fun skipToPrevious()

    fun skipToNext()

    fun play()

    fun pause()

    fun stop()

    fun addNextPlay(song: SongBean)

    fun deleteSong(song: SongBean)

    fun clearPlayList()
}