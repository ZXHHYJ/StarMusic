package studio.mandysa.music.ui.screen.singer.popularsong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.network.api

class PopularSongViewModel(id: String) : ViewModel() {
    val songs = flow {
        emit(api.getSingerHotSong(id))
    }.asLiveData(viewModelScope.coroutineContext)
}