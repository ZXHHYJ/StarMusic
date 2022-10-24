package studio.mandysa.music.logic.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.drake.serialize.serialize.serialLiveData
import studio.mandysa.music.logic.bean.UserBean

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

    /**
     * 网易云账号登录信息
     */
    private val mNeteaseCloudUserBean by serialLiveData<UserBean>(
        default = null,
        name = "netease_cloud_user_bean"
    )

    val userCookie
        get() = mNeteaseCloudUserBean.value!!.cookie

    val userId
        get() = mNeteaseCloudUserBean.value!!.userId

    val isLoginNeteaseCloud: LiveData<Boolean> = mEnableNeteaseCloud.map { it != null }

    val enableNeteaseCloud: LiveData<Boolean> = mEnableNeteaseCloud

    val neteaseCloudPath: LiveData<String> = mNeteaseCloudPath

    fun setEnableNeteaseCloud(enable: Boolean) {
        mEnableNeteaseCloud.value = enable
    }

    fun setEnableNeteasePath(path: String) {
        mNeteaseCloudPath.value = path
    }

    fun setNeteaseCloudUserBean(userid: String, cookie: String) {
        mNeteaseCloudUserBean.value = UserBean(userId, cookie)
    }

}