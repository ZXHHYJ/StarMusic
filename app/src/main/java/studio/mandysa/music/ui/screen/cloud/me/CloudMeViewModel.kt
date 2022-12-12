package studio.mandysa.music.ui.screen.cloud.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.bean.MyDigitalAlbumBean
import studio.mandysa.music.logic.bean.UserBean
import studio.mandysa.music.logic.bean.UserPlaylistBean

class CloudMeViewModel : ViewModel() {

    private val mUserInfoLiveData = MutableLiveData<UserBean>()

    val userInfoLiveData: LiveData<UserBean> = mUserInfoLiveData

    private val mUserPlaylistBeanLiveData = MutableLiveData<List<UserPlaylistBean>>()

    val userPlaylistBeanLiveData: LiveData<List<UserPlaylistBean>> = mUserPlaylistBeanLiveData

    private val mBeanMyDigitalAlbumsLiveData = MutableLiveData<List<MyDigitalAlbumBean>>()

    val myDigitalAlbumsLiveDataBean: LiveData<List<MyDigitalAlbumBean>> = mBeanMyDigitalAlbumsLiveData

    suspend fun refresh() {
        mUserInfoLiveData.value = api.network().getUserInfo()
        mUserPlaylistBeanLiveData.value = api.network().getUserPlaylist()
        mBeanMyDigitalAlbumsLiveData.value = api.network().getMyDigitalAlbum()
        //println(api.network().getRecentSongs())
    }

}