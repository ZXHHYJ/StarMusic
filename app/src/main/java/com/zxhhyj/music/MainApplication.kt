package com.zxhhyj.music

import android.app.Application
import android.content.Intent
import android.os.Build
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.squareup.moshi.Moshi
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlin.system.exitProcess


class MainApplication : Application() {

    companion object {
        lateinit var context: Application
            private set
    }

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
        //初始化Moshi
        val moshi = Moshi.Builder().build()

        val adapterWebDavFile = moshi.adapter(WebDavFile::class.java)
        registerTypeConverters(
            save = { bean -> adapterWebDavFile.toJson(bean) },
            restore = { str -> adapterWebDavFile.fromJson(str) }
        )

        val adapterFolder = moshi.adapter(Folder::class.java)
        registerTypeConverters(
            save = { bean -> adapterFolder.toJson(bean) },
            restore = { str -> adapterFolder.fromJson(str) }
        )

        val adapterSongBeanLocal = moshi.adapter(SongBean.Local::class.java)
        registerTypeConverters(
            save = { bean -> adapterSongBeanLocal.toJson(bean) },
            restore = { str -> adapterSongBeanLocal.fromJson(str) }
        )

        val adapterSongBeanWebDav = moshi.adapter(SongBean.WebDav::class.java)
        registerTypeConverters(
            save = { bean -> adapterSongBeanWebDav.toJson(bean) },
            restore = { str -> adapterSongBeanWebDav.fromJson(str) }
        )

        val adapterSongBeanAlbum = moshi.adapter(SongBean.Album::class.java)
        registerTypeConverters(
            save = { bean -> adapterSongBeanAlbum.toJson(bean) },
            restore = { str -> adapterSongBeanAlbum.fromJson(str) }
        )

        val adapterSongBeanArtist = moshi.adapter(SongBean.Artist::class.java)
        registerTypeConverters(
            save = { bean -> adapterSongBeanArtist.toJson(bean) },
            restore = { str -> adapterSongBeanArtist.fromJson(str) }
        )

        val adapterWebDavConfig = moshi.adapter(WebDavConfig::class.java)
        registerTypeConverters(
            save = { bean -> adapterWebDavConfig.toJson(bean) },
            restore = { str -> adapterWebDavConfig.fromJson(str) }
        )

        val adapterIntArray = moshi.adapter(IntArray::class.java)
        registerTypeConverters(
            save = { bean -> adapterIntArray.toJson(bean) },
            restore = { str -> adapterIntArray.fromJson(str) }
        )

        val adapterPlayMode = moshi.adapter(PlayerManager.PlayMode::class.java)
        registerTypeConverters(
            save = { bean -> adapterPlayMode.toJson(bean) },
            restore = { str -> adapterPlayMode.fromJson(str) }
        )

        val adapterPlayListBean = moshi.adapter(PlayListBean::class.java)
        registerTypeConverters(
            save = { bean -> adapterPlayListBean.toJson(bean) },
            restore = { str -> adapterPlayListBean.fromJson(str) }
        )

        val adapterPlayListSongBean = moshi.adapter(PlayListSongBean::class.java)
        registerTypeConverters(
            save = { bean -> adapterPlayListSongBean.toJson(bean) },
            restore = { str -> adapterPlayListSongBean.fromJson(str) }
        )

        val adapterPlayMemoryBean = moshi.adapter(PlayMemoryBean::class.java)
        registerTypeConverters(
            save = { bean -> adapterPlayMemoryBean.toJson(bean) },
            restore = { str -> adapterPlayMemoryBean.fromJson(str) }
        )
        //确保播放音乐时播放启动服务
        PlayerManager.pauseLiveData()
            .observeForever {
                if (it == false)
                    startPlayerService()
            }
        PlayerManager.currentSongLiveData()
            .observeForever {
                if (it != null)
                    startPlayerService()
            }
        //app启动后自动播放音乐
        if (SettingRepository.EnableAutoPlayMusic) {
            MediaLibHelper.songs.takeIf { it.isNotEmpty() }?.run {
                PlayerManager.play(this, 0)
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