package studio.mandysa.music.ui.screen.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.MyDigitalAlbum
import studio.mandysa.music.logic.model.RecentSongModel
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylist

class MeViewModel : ViewModel() {

    private val mUserInfoLiveData = MutableLiveData<UserModel>()

    val userInfoLiveData: LiveData<UserModel> = mUserInfoLiveData

    private val mUserPlaylistLiveData = MutableLiveData<List<UserPlaylist>>()

    val userPlaylistLiveData: LiveData<List<UserPlaylist>> = mUserPlaylistLiveData

    private val mMyDigitalAlbumsLiveData = MutableLiveData<List<MyDigitalAlbum>>()

    val myDigitalAlbumsLiveData: LiveData<List<MyDigitalAlbum>> = mMyDigitalAlbumsLiveData

    private val mRecentSongsLiveData = MutableLiveData<List<RecentSongModel>>()

    val recentSongsLiveData: LiveData<List<RecentSongModel>> = mRecentSongsLiveData

    suspend fun refresh() {
        mUserInfoLiveData.value = api.network().getUserInfo()
        api.network().getUserPlaylist().let { list ->
            val newList = list.toMutableList()
            list.forEach {
                //删除不是自己创建的歌单
                if (it.nickname != mUserInfoLiveData.value?.nickname) {
                    newList.remove(it)
                }
            }
            //删除我喜欢的歌单
            newList.removeAt(0)
            mUserPlaylistLiveData.value = newList
        }
        mMyDigitalAlbumsLiveData.value = api.network().getMyDigitalAlbum()
        mRecentSongsLiveData.value = api.network().getRecentSongs(limit = 10)
    }

}