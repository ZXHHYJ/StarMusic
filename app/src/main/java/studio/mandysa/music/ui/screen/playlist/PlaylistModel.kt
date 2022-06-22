package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class PlaylistModel(private val id: String) : ViewModel() {

    val songs = Pager(PagingConfig(pageSize = 30)) {
        PlaylistPagingSource(id)
    }.flow.cachedIn(viewModelScope)

    val playlistInfoModel = flow {
        emit(api.getSongListInfo(id = id))
    }.flowOn(Dispatchers.IO).asLiveData()

    /* init {
         viewModelScope.launch(Dispatchers.IO) {
             println(api.getPlaylistSongs(id = id, limit = 30, offset = 0))
         }
     }*/

}