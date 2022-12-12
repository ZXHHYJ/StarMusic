package studio.mandysa.music.ui.screen.cloud.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.bean.BannerBean
import studio.mandysa.music.logic.bean.PlaylistBean
import studio.mandysa.music.logic.bean.RecommendSongBean
import studio.mandysa.music.logic.bean.UserBean
import studio.mandysa.music.ui.screen.cloud.me.CloudMeViewModel

class CloudViewModel : ViewModel() {

    private val mUserInfoLiveData = MutableLiveData<UserBean>()

    /**
     * 获取用户信息，参考[CloudMeViewModel]
     */
    val userInfoLiveData: LiveData<UserBean> = mUserInfoLiveData

    private val mBannersLiveData = MutableLiveData<List<BannerBean>>()

    val bannersLiveData: LiveData<List<BannerBean>> = mBannersLiveData

    private val mRecommendSongBeanLiveData = MutableLiveData<List<RecommendSongBean>>()

    val recommendSongBeanLiveData: LiveData<List<RecommendSongBean>> = mRecommendSongBeanLiveData

    private val mRecommendPlaylistLiveData = MutableLiveData<List<PlaylistBean>>()

    val recommendPlaylistLiveData: LiveData<List<PlaylistBean>> = mRecommendPlaylistLiveData

    private val mPlaylistSquareLiveData = MutableLiveData<List<PlaylistBean>>()

    val playlistSquareLiveData: LiveData<List<PlaylistBean>> = mPlaylistSquareLiveData

    suspend fun network() {
        mUserInfoLiveData.value = api.network().getUserInfo()

        mBannersLiveData.value = api.network().getBannerList()
        mRecommendSongBeanLiveData.value = api.network().getRecommendSong()
        mRecommendPlaylistLiveData.value = api.network().getRecommendPlaylist()
        mPlaylistSquareLiveData.value = api.network().getPlaylistSquare()
    }

}