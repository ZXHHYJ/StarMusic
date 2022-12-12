package studio.mandysa.music.logic.repository

import androidx.lifecycle.LiveData
import com.drake.serialize.serialize.serialLiveData
import studio.mandysa.music.logic.bean.SerialUserBean

object SettingRepository {

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
    private val mNeteaseCloudUserBean by serialLiveData<SerialUserBean>(
        default = null,
        name = "netease_cloud_user_bean"
    )

    val userCookie
        get() = mNeteaseCloudUserBean.value!!.cookie

    val userId
        get() = mNeteaseCloudUserBean.value!!.userId

    val neteaseCloudPath: LiveData<String> = mNeteaseCloudPath

    fun setEnableNeteasePath(path: String) {
        mNeteaseCloudPath.value = path
    }

    fun setNeteaseCloudUserBean(userid: String, cookie: String) {
        mNeteaseCloudUserBean.value = SerialUserBean(userId, cookie)
    }

}