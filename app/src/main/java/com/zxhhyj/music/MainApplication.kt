package com.zxhhyj.music

import android.app.Application
import android.content.Intent
import android.os.Build
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.google.gson.GsonBuilder
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.bean.PlayListSongBean
import com.zxhhyj.music.logic.bean.PlayMemoryBean
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavSource
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.StarMusicService
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.theme.PaletteStyle
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

        val gson = GsonBuilder().create()

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, Folder::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.Local::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SongBean.WebDav::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, IntArray::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, PlayerManager.PlayMode::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, PlayListBean::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, PlayListSongBean::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, PlayMemoryBean::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SettingRepository.ThemeModeEnum::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, SettingRepository.DarkModeEnum::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, PaletteStyle::class.java) }
        )

        registerTypeConverters(
            save = { bean -> gson.toJson(bean) },
            restore = { str -> gson.fromJson(str, WebDavSource::class.java) }
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

        //记忆播放模式
        PlayerManager.setPlayMode(SettingRepository.PlayMode)
        //记忆播放列表和下标
        SettingRepository.PlayMemory
            ?.takeIf { SettingRepository.EnablePlayMemory }
            ?.let { it ->
                if (it.index == null) return
                if (it.songInSongsIndexList == null) return
                val list = it.songInSongsIndexList.mapNotNull {
                    try {
                        MediaLibHelper.songs[it]
                    } catch (_: Exception) {
                        null
                    }
                }
                PlayerManager.install(list, it.index)
            }

        //app启动后自动播放音乐
        if (SettingRepository.EnableAutoPlayMusic) {
            PlayerManager.playListFlow.value?.takeIf { it.isNotEmpty() }?.let {
                PlayerManager.start()
            } ?: run {
                MediaLibHelper.songs.takeIf { it.isNotEmpty() }?.run {
                    PlayerManager.play(this, 0)
                }
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