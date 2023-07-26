package com.zxhhyj.music.service.playmanager.impl

import androidx.lifecycle.LiveData
import com.zxhhyj.music.service.playmanager.bean.SongBean

interface PlayManagerState {
    // 获取当前播放列表
    fun changePlayListLiveData(): LiveData<List<SongBean>?>

    // 获取当前播放进度
    fun progressLiveData(): LiveData<Int>

    // 获取当前歌曲总时长
    fun durationLiveData(): LiveData<Int>

    // 获取当前播放的歌曲
    fun changeMusicLiveData(): LiveData<SongBean?>

    // 获取当前是否处于暂停状态
    val isPaused: Boolean

    // 获取当前暂停状态
    fun pauseLiveData(): LiveData<Boolean>
}