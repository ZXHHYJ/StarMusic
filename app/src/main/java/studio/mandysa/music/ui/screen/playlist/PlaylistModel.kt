package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.network.api

class PlaylistModel(private val id: String) : ViewModel() {

    val songs = flow {
        emit(api.getPlaylistSongs(id = id))
    }.asLiveData(viewModelScope.coroutineContext)

    val playlistInfoModel = flow {
        emit(api.getSongListInfo(id = id))
    }.asLiveData(viewModelScope.coroutineContext)

}