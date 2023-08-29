package com.zxhhyj.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.google.gson.GsonBuilder
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.MediaPlayService
import com.zxhhyj.music.service.playmanager.PlayManager
import io.fastkv.FastKVConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class MainApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        //全局context
        context = this
        //初始化FastKV
        FastKVConfig.setExecutor(Dispatchers.Default.asExecutor())
        //初始化Gson
        val gson = GsonBuilder().create()
        registerTypeConverters<SongBean>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean::class.java) }
        )
        registerTypeConverters<SongBean.Album>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Album::class.java) }
        )
        registerTypeConverters<SongBean.Artist>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Artist::class.java) }
        )
        @Suppress("RemoveExplicitTypeArguments")
        registerTypeConverters<PlayListModel>(
            save = { bean -> bean.uuid },
            restore = { str -> PlayListModel(str) }
        )
        //初始化播放管理器
        PlayManager.pauseLiveData()
            .observeForever {
                if (it == false)
                    startPlayerService()
            }
        //确保播放音乐时播放启动服务
        PlayManager.currentSongLiveData()
            .observeForever {
                if (it != null)
                    startPlayerService()
            }
        //app启动后自动播放音乐功能
        if (SettingRepository.EnableAutoPlayMusic) {
            AndroidMediaLibsRepository.songs.takeIf { it.isNotEmpty() }?.run {
                PlayManager.play(this, 0)
            }
        }
    }

    @Synchronized
    private fun startPlayerService() {
        if (!MediaPlayService.isServiceAlive) {
            val intent = Intent(this, MediaPlayService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }

}