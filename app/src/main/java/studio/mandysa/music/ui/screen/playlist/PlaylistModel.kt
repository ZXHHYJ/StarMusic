package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.network.ServiceCreator

class PlaylistModel(private val id: String) : ViewModel() {

    val songs = Pager(PagingConfig(pageSize = 15)) {
        PlaylistPagingSource(id)
    }.flow.cachedIn(viewModelScope)

    val playlistInfoModel: Flow<PlaylistInfoModel> = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getSongListInfo(id = id))
    }.flowOn(Dispatchers.IO)

}