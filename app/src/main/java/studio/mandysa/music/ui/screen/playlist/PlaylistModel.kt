package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import studio.mandysa.music.logic.network.api

class PlaylistModel(private val id: String) : ViewModel() {

    val songs = Pager(PagingConfig(pageSize = 15)) {
        PlaylistPagingSource(id)
    }.flow.cachedIn(viewModelScope)

    val playlistInfoModel = flow {
        emit(api.getSongListInfo(id = id))
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)
}