package studio.mandysa.music

import android.app.Application
import android.content.Intent
import studio.mandysa.fastkt.FastKt
import studio.mandysa.music.logic.config.cache
import studio.mandysa.music.logic.config.noBackup
import studio.mandysa.music.logic.ktx.playManager
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.music.service.playmanager.PlayManager

/**
 * @author 黄浩
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        noBackup = FastKt(noBackupFilesDir.toString(), "FastKt")
        cache = FastKt(cacheDir.toString(), "FastKt")

        PlayManager.init(this)
        //初始化播放管理器
        playManager {
            //确保播放音乐时播放启动服务
            pauseLiveData()
                .observeForever {
                    if (it == false)
                        startPlayerService()
                }
            changeMusicLiveData()
                .observeForever {
                    if (it != null)
                        startPlayerService()
                }
        }
    }

    private fun startPlayerService() {
        if (MediaPlayService.instance == null) {
            try {
                startForegroundService(
                    Intent(
                        this,
                        MediaPlayService::class.java
                    )
                )
            } catch (e: Exception) {
            }
        }
    }

}