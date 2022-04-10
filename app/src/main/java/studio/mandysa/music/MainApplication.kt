package studio.mandysa.music

import android.app.Application
import android.content.Intent
import simon.tuke.Tuke
import studio.mandysa.music.logic.ktx.playManager
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.statelayout.StateLayout

/**
 * @author liuxiaoliu66
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //初始化Tuke
        Tuke.init(this)

        //配置StateLayout
        StateLayout.let {
            it.loadingLayout = R.layout.layout_loading
            it.emptyLayout = R.layout.layout_empty
            it.errorLayout = R.layout.layout_error
            it.setRetryId(R.id.cl_error_check)
        }

        //初始化播放管理器
        PlayManager.init(this)
        //确保播放音乐时播放启动服务
        playManager {
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
            startForegroundService(
                Intent(
                    this,
                    MediaPlayService::class.java
                )
            )
        }
    }

}