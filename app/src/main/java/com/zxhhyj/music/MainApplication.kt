package com.zxhhyj.music

import android.app.Application
import android.content.Intent
import android.os.Build
import com.alibaba.fastjson2.JSON
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.bean.PlayListSongBean
import com.zxhhyj.music.logic.bean.PlayMemoryBean
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavConfig
import com.zxhhyj.music.logic.bean.WebDavFile
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.StarMusicService
import com.zxhhyj.music.service.playermanager.PlayerManager
import io.fastkv.FastKVConfig
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.system.exitProcess


class MainApplication : Application() {

    companion object {
        lateinit var context: Application
            private set
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        context = this
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            e.printStackTrace()
            CrashActivity.startActivity(this, e.stackTraceToString())
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
        //初始化FastKV
        FastKVConfig.setExecutor(Dispatchers.Default.asExecutor())

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, WebDavFile::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, Folder::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, SongBean.Local::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, SongBean.WebDav::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, SongBean.Album::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, SongBean.Artist::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, WebDavConfig::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, IntArray::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, PlayerManager.PlayMode::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, PlayListBean::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, PlayListSongBean::class.java) }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str ->
                JSON.parseObject(str, PlayMemoryBean::class.java)
            }
        )

        registerTypeConverters(
            save = { bean -> JSON.toJSONString(bean) },
            restore = { str -> JSON.parseObject(str, SettingRepository.ThemeModeClass::class.java) }
        )
        //确保播放音乐时播放启动服务
        PlayerManager.pauseFlow.onEach {
            if (!it) {
                startPlayerService()
            }
        }.launchIn(GlobalScope)
        PlayerManager.currentSongFlow.onEach {
            if (it != null)
                startPlayerService()
        }.launchIn(GlobalScope)
        //app启动后自动播放音乐
        if (SettingRepository.EnableAutoPlayMusic) {
            MediaLibHelper.songs.takeIf { it.isNotEmpty() }?.run {
                PlayerManager.play(this, 0)
            }
        }

        //记忆播放模式
        PlayerManager.setPlayMode(SettingRepository.PlayMode)
        //记忆播放列表和下标
        SettingRepository.PlayMemory
            ?.takeIf { SettingRepository.EnablePlayMemory }
            ?.takeIf { it.index != null }
            ?.takeIf { it.playlist != null }
            ?.let {
                PlayerManager.install(it.playlist!!, it.index!!)
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