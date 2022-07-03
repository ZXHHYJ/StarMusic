package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import simon.tuke.livedata
import studio.mandysa.music.logic.model.PlaylistSong
import studio.mandysa.music.logic.network.api

class PlaylistViewModel(private val id: String) : ViewModel() {

    private val mSongsLiveData by livedata<ArrayList<PlaylistSong>>(
        def = arrayListOf(),
        key = id,
        isCache = true
    )

    val songsLiveData: LiveData<ArrayList<PlaylistSong>> = mSongsLiveData

    init {
        viewModelScope.launch {
            if (mSongsLiveData.value?.isEmpty() == true)
                mSongsLiveData.value = api.getPlaylistSongs(id = id)
        }
    }

    val playlistInfoModel = flow {
        emit(api.getSongListInfo(id = id))
    }.asLiveData(viewModelScope.coroutineContext)

}