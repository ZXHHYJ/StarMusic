package studio.mandysa.music.logic.repository

import androidx.lifecycle.LiveData
import com.drake.serialize.serialize.serialLiveData

object SettingRepository {
    /**
     * 启用在线音乐
     */
    private val mEnableNeteaseCloud by serialLiveData(
        default = false,
        name = "enable_netease_cloud"
    )

    /**
     * 设置网易云后端地址
     */
    private val mNeteaseCloudPath by serialLiveData<String>(
        default = null,
        name = "netease_cloud_path"
    )

    val enableNeteaseCloud: LiveData<Boolean> = mEnableNeteaseCloud

    val neteaseCloudPath: LiveData<String> = mNeteaseCloudPath

    fun setEnableNeteaseCloud(enable: Boolean) {
        mEnableNeteaseCloud.value = enable
    }

    fun setEnableNeteasePath(path: String) {
        mNeteaseCloudPath.value = path
    }

}