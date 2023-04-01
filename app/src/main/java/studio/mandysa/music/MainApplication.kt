package studio.mandysa.music

import android.app.Application
import android.content.Intent
import android.os.Build
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import studio.mandysa.music.logic.config.application
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean

/**
 * @author 黄浩
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
        System.loadLibrary("monet")
        //loading lib monet
        MMKV.initialize(this)
        //初始化mmkv
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
        if (MediaPlayService.instance == null) {
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