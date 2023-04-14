package com.zxhhyj.music

import android.app.Application
import android.content.Intent
import android.os.Build
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import com.zxhhyj.music.logic.config.application
import com.zxhhyj.music.service.MediaPlayService
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean


class MainApplication : Application() {

    companion object {
        init {
            System.loadLibrary("monet")
            //loading lib monet
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        MMKV.initialize(this)
        val gson = GsonBuilder().create()
        registerTypeConverters<SongBean.Local>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Local::class.java) }
        )
        registerTypeConverters<SongBean.Local.Album>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Local.Album::class.java) }
        )
        registerTypeConverters<SongBean.Local.Artist>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Local.Artist::class.java) }
        )
        PlayManager.init(this)
        //初始化播放管理器
        PlayManager.pauseLiveData()
            .observeForever {
                if (it == false)
                    startPlayerService()
            }
        PlayManager.changeMusicLiveData()
            .observeForever {
                if (it != null)
                    startPlayerService()
            }
        //确保播放音乐时播放启动服务
    }

    private fun startPlayerService() {
        if (!MediaPlayService.isServiceAlive) {
            Intent(this, MediaPlayService::class.java).let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(it)
                } else {
                    startService(it)
                }
            }
        }
    }

}