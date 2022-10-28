package studio.mandysa.music.ui.screen.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.bean.PlaylistInfoBean
import studio.mandysa.music.logic.bean.PlaylistSongBean

class ListViewModel(val id: String) : ViewModel() {
    private val mSongsLiveData = MutableLiveData<List<PlaylistSongBean>>()

    val songsLiveData: LiveData<List<PlaylistSongBean>> = mSongsLiveData

    private val mPlaylistInfoBeanLiveData = MutableLiveData<PlaylistInfoBean>()

    val playlistInfoBeanLiveData: LiveData<PlaylistInfoBean> = mPlaylistInfoBeanLiveData

    suspend fun refresh() {
        mPlaylistInfoBeanLiveData.value = api.network().getSongListInfo(id = id)
        mSongsLiveData.value = api.network().getPlaylistSongs(id = id)
    }
}