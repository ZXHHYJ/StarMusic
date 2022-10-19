package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.model.PlaylistSong
import studio.mandysa.music.logic.repository.PlaylistRepository

class PlaylistViewModel(private val id: String) : ViewModel() {
    private val mRepository = PlaylistRepository(id)

    val songsLiveData: MutableLiveData<ArrayList<PlaylistSong>?> = mRepository.songsLiveData

    val infoModelLiveData: MutableLiveData<PlaylistInfoModel?> = mRepository.infoLiveData

    suspend fun refresh() {
        mRepository.infoLiveData.value = api.network().getSongListInfo(id = id)
        mRepository.songsLiveData.value = api.network().getPlaylistSongs(id = id)
    }

}