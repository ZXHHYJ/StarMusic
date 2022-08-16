package studio.mandysa.music

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.snapshotFlow
import com.drake.net.NetConfig
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.Cache
import studio.mandysa.fastkt.FastKt
import studio.mandysa.music.logic.config.BASE_URL
import studio.mandysa.music.logic.config.cacheFastKt
import studio.mandysa.music.logic.config.mainApplication
import studio.mandysa.music.logic.config.noBackupFastKt
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.music.service.playmanager.PlayManager

/**
 * @author 黄浩
 */
class MainApplication : Application() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        noBackupFastKt = FastKt(noBackupFilesDir.toString(), "FastKt")

        cacheFastKt = FastKt(cacheDir.toString(), "FastKt")

        mainApplication = this

        NetConfig.initialize(BASE_URL, this) {
            // ...
            // 本框架支持Http缓存协议和强制缓存模式
            cache(Cache(cacheDir, 1024 * 1024 * 128)) // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小
        }
        //初始化播放管理器
        PlayManager.init(this)
        //确保播放音乐时播放启动服务
        snapshotFlow {
            PlayManager.pause
        }.onEach {
            if (it == false) {
                startPlayerService()
            }
        }.launchIn(GlobalScope)

        snapshotFlow {
            PlayManager.changeMusic
        }.onEach {
            if (it != null) {
                startPlayerService()
            }
        }.launchIn(GlobalScope)
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