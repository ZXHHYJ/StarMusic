package studio.mandysa.music.ui.screen.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.MyDigitalAlbum
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylist
import studio.mandysa.music.ui.common.SwipeRefreshViewModel

class MeViewModel : SwipeRefreshViewModel() {

    private val mUserInfoLiveData = MutableLiveData<UserModel>()

    val userInfoLiveData: LiveData<UserModel> = mUserInfoLiveData

    private val mUserPlaylistLiveData = MutableLiveData<List<UserPlaylist>>()

    val userPlaylistLiveData: LiveData<List<UserPlaylist>> = mUserPlaylistLiveData

    private val mMyDigitalAlbumsLiveData = MutableLiveData<List<MyDigitalAlbum>>()

    val myDigitalAlbumsLiveData: LiveData<List<MyDigitalAlbum>> = mMyDigitalAlbumsLiveData

    override suspend fun preview() {
        isRefreshing.value = true
        refresh()
        isRefreshing.value = false
    }

    override suspend fun refresh() {
        mUserInfoLiveData.value = api.network().getUserInfo()
        mUserPlaylistLiveData.value = api.network().getUserPlaylist()
        mMyDigitalAlbumsLiveData.value = api.network().getMyDigitalAlbum()
        println(api.network().getRecentSongs())
    }

}