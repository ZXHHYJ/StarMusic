package studio.mandysa.music.ui.screen.singer.popularsong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class PopularSongViewModel(id: String) : ViewModel() {
    val songs = flow {
        emit(api.getSingerHotSong(id))
    }.flowOn(Dispatchers.IO).asLiveData()
}