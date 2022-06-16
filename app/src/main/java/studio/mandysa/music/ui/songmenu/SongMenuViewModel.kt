package studio.mandysa.music.ui.songmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class SongMenuViewModel(id: String) : ViewModel() {
    val songInfo = flow {
        emit(api.getSongInfo(ids = listOf(id))[0])
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)

    val isLike = flow {
        emit(api.getLikeList().indexOf(id) != -1)
    }.flowOn(Dispatchers.IO)

    init {
        isLike.conflate()
    }
}