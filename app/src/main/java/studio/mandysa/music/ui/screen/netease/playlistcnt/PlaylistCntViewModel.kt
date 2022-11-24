package studio.mandysa.music.ui.screen.netease.playlistcnt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.bean.PlaylistInfoBean
import studio.mandysa.music.logic.bean.PlaylistSongBean
import studio.mandysa.music.logic.repository.PlaylistRepository

class PlaylistCntViewModel(private val id: String) : ViewModel() {
    private val mRepository = PlaylistRepository(id)

    val songsLiveData: MutableLiveData<ArrayList<PlaylistSongBean>?> = mRepository.songsLiveData

    val infoModelLiveData: MutableLiveData<PlaylistInfoBean?> = mRepository.infoLiveData

    suspend fun refresh() {
        mRepository.infoLiveData.value = api.network().getSongListInfo(id = id)
        mRepository.songsLiveData.value = api.network().getPlaylistSongs(id = id)
    }

}