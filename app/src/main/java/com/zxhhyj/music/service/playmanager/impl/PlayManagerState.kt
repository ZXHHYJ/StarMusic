package com.zxhhyj.music.service.playmanager.impl

import androidx.lifecycle.LiveData
import com.zxhhyj.music.service.playmanager.bean.SongBean

interface PlayManagerState {
    fun changePlayListLiveData(): LiveData<List<SongBean>?>

    fun playingMusicProgressLiveData(): LiveData<Int>

    fun playingMusicDurationLiveData(): LiveData<Int>

    fun changeMusicLiveData(): LiveData<SongBean?>

    fun isPaused(): Boolean

    fun pauseLiveData(): LiveData<Boolean>
}