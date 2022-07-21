package studio.mandysa.music.ui.screen.me.meplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.config.api

class MePlaylistViewModel : ViewModel() {

    //获取用户的所以歌单，*网易云把我喜欢也算在里面
    val meAllPlaylist = flow {
        emit(api.getUserPlaylist())
    }.asLiveData(context = viewModelScope.coroutineContext)

}