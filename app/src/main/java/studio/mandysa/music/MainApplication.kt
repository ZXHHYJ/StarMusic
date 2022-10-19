package studio.mandysa.music

import android.app.Application
import android.content.Intent
import android.os.Build
import com.drake.net.NetConfig
import okhttp3.Cache
import studio.mandysa.music.logic.config.BASE_URL
import studio.mandysa.music.logic.config.mainApplication
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.music.service.playmanager.PlayManager

/**
 * @author 黄浩
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        mainApplication = this

        NetConfig.initialize(BASE_URL, this) {
            cache(Cache(cacheDir, 1024 * 1024 * 128))
            // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小
        }
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