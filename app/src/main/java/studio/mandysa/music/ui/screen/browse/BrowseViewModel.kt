package studio.mandysa.music.ui.screen.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import studio.mandysa.music.logic.network.api

class BrowseViewModel : ViewModel() {

    val banners = flow {
        emit(api.getBannerList())
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)

    val recommendSongs = flow {
        emit(api.getRecommendedSong())
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)

    val recommendPlaylist = flow {
        emit(api.getRecommendPlaylist())
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)


    val playlistSquare = flow {
        emit(api.getPlaylistSquare())
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)
}