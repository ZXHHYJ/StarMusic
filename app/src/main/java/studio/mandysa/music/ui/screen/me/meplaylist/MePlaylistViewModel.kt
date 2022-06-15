package studio.mandysa.music.ui.screen.me.meplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import studio.mandysa.music.logic.network.api

class MePlaylistViewModel : ViewModel() {

    //获取用户的所以歌单，*网易云把我喜欢也算在里面
    val meAllPlaylist = flow {
        emit(api.getUserPlaylist())
    }.flowOn(Dispatchers.IO).shareIn(viewModelScope, SharingStarted.WhileSubscribed(500), 0).asLiveData()

}