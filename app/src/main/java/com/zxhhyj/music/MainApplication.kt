package com.zxhhyj.music

import android.app.Application
import android.content.Intent
import android.os.Build
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.google.gson.GsonBuilder
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavConfig
import com.zxhhyj.music.logic.bean.WebDavFile
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.StarMusicService
import com.zxhhyj.music.service.playmanager.PlayManager
import io.fastkv.FastKVConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlin.system.exitProcess

class MainApplication : Application() {

    companion object {
        lateinit var context: Application
            private set
        var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    }

    override fun onCreate() {
        super.onCreate()
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, e ->
                e.printStackTrace()
                CrashActivity.startActivity(this, e.stackTraceToString())
                exitProcess(0)
            }
        }
        //全局context
        context = this
        //初始化FastKV
        FastKVConfig.setExecutor(Dispatchers.Default.asExecutor())
        //初始化Gson
        val gson = GsonBuilder().create()
        registerTypeConverters<WebDavFile>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, WebDavFile::class.java) }
        )
        registerTypeConverters<Folder>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, Folder::class.java) }
        )
        registerTypeConverters<SongBean.Local>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Local::class.java) }
        )
        registerTypeConverters<SongBean.WebDav>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.WebDav::class.java) }
        )
        registerTypeConverters<SongBean.Album>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Album::class.java) }
        )
        registerTypeConverters<SongBean.Artist>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Artist::class.java) }
        )
        registerTypeConverters<WebDavConfig>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, WebDavConfig::class.java) }
        )
        @Suppress("RemoveExplicitTypeArguments")
        registerTypeConverters<PlayListModel>(
            save = { bean -> bean.uuid },
            restore = { str -> PlayListModel(str) }
        )
        registerTypeConverters<IntArray>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, IntArray::class.java) }
        )
        registerTypeConverters<PlayManager.PlayMode>(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, PlayManager.PlayMode::class.java) }
        )
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
        //app启动后自动播放音乐
        if (SettingRepository.EnableAutoPlayMusic) {
            MediaLibHelper.songs.takeIf { it.isNotEmpty() }?.run {
                PlayManager.play(this, 0)
            }
        }
    }

    @Synchronized
    private fun startPlayerService() {
        if (!StarMusicService.isServiceAlive) {
            val intent = Intent(this, StarMusicService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }

}