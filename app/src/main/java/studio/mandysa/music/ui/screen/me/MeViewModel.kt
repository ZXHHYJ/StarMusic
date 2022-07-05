package studio.mandysa.music.ui.screen.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.globalLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.config.cache
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylist
import studio.mandysa.music.ui.common.SwipeRefreshViewModel

class MeViewModel : SwipeRefreshViewModel() {

    private val mUserInfoLiveData by globalLiveData<UserModel>(
        key = "user_model",
        def = null,
        fastKt = cache
    )

    val userInfoLiveData: LiveData<UserModel> = mUserInfoLiveData

    private val mAllPlaylistLiveData by globalLiveData<ArrayList<UserPlaylist>>(
        key = "all_playlist",
        def = null,
        fastKt = cache
    )

    val allPlaylistLiveData: LiveData<ArrayList<UserPlaylist>> = mAllPlaylistLiveData

    override fun refresh() {
        viewModelScope.launch {
            mUserInfoLiveData.value = api.getUserInfo()
            mAllPlaylistLiveData.value = api.getUserPlaylist()
        }
    }

    override fun isRefresh(): Boolean {
        return mUserInfoLiveData.value == null || mAllPlaylistLiveData.value == null
    }

}