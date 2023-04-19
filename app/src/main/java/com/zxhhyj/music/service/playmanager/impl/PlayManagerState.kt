package com.zxhhyj.music.service.playmanager.impl

import androidx.lifecycle.LiveData
import com.zxhhyj.music.service.playmanager.bean.SongBean

interface PlayManagerState {
    fun changePlayListLiveData(): LiveData<List<SongBean>?>

    fun progressLiveData(): LiveData<Int>

    fun durationLiveData(): LiveData<Int>

    fun changeMusicLiveData(): LiveData<SongBean?>

    val isPaused: Boolean

    fun pauseLiveData(): LiveData<Boolean>
}