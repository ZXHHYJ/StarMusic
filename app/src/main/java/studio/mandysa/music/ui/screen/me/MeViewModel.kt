package studio.mandysa.music.ui.screen.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylist
import studio.mandysa.music.ui.common.SwipeRefreshViewModel

class MeViewModel : SwipeRefreshViewModel() {

    private val mUserInfoLiveData = MutableLiveData<UserModel>()

    val userInfoLiveData: LiveData<UserModel> = mUserInfoLiveData

    private val mAllPlaylistLiveData = MutableLiveData<List<UserPlaylist>>()

    val allPlaylistLiveData: LiveData<List<UserPlaylist>> = mAllPlaylistLiveData

    override suspend fun preview() {
        mUserInfoLiveData.value = api.cache().getUserInfo()
        mAllPlaylistLiveData.value = api.cache().getUserPlaylist()
    }

    override suspend fun refresh() {
        mUserInfoLiveData.value = api.network().getUserInfo()
        mAllPlaylistLiveData.value = api.network().getUserPlaylist()
    }

}