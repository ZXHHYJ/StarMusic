package studio.mandysa.music.ui.screen.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.BannerModel
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendSong
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.ui.screen.me.MeViewModel

class BrowseViewModel : ViewModel() {

    private val mUserInfoLiveData = MutableLiveData<UserModel>()

    /**
     * 获取用户信息，参考[MeViewModel]
     */
    val userInfoLiveData: LiveData<UserModel> = mUserInfoLiveData

    private val mBannersLiveData = MutableLiveData<List<BannerModel>>()

    val bannersLiveData: LiveData<List<BannerModel>> = mBannersLiveData

    private val mRecommendSongLiveData = MutableLiveData<List<RecommendSong>>()

    val recommendSongLiveData: LiveData<List<RecommendSong>> = mRecommendSongLiveData

    private val mRecommendPlaylistLiveData = MutableLiveData<List<PlaylistModel>>()

    val recommendPlaylistLiveData: LiveData<List<PlaylistModel>> = mRecommendPlaylistLiveData

    private val mPlaylistSquareLiveData = MutableLiveData<List<PlaylistModel>>()

    val playlistSquareLiveData: LiveData<List<PlaylistModel>> = mPlaylistSquareLiveData

    suspend fun network() {
        mUserInfoLiveData.value = api.network().getUserInfo()

        mBannersLiveData.value = api.network().getBannerList()
        mRecommendSongLiveData.value = api.network().getRecommendSong()
        mRecommendPlaylistLiveData.value = api.network().getRecommendPlaylist()
        mPlaylistSquareLiveData.value = api.network().getPlaylistSquare()
    }

}