package studio.mandysa.music.ui.screen.singer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.config.api

class SingerViewModel(id: String) : ViewModel() {
    val singerInfo = flow {
        emit(api.getSingerDetails(id = id))
    }.asLiveData(viewModelScope.coroutineContext)

    val albumSource = Pager(PagingConfig(pageSize = 30)) {
        SingerAlbumPagingSource(id)
    }.flow.cachedIn(viewModelScope)

    val songs = flow {
        emit(api.getSingerHotSong(id))
    }.asLiveData(viewModelScope.coroutineContext)
}