package studio.mandysa.music.ui.screen.singer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.SingerDetailedModel

class SingerViewModel(private val id: String) : ViewModel() {

    var singerDetails by mutableStateOf<SingerDetailedModel?>(null)
        private set
    var singerHotSongs by mutableStateOf<List<MusicModel>?>(null)
        private set

    val albumSource = Pager(PagingConfig(pageSize = 30)) {
        SingerAlbumPagingSource(id)
    }.flow.cachedIn(viewModelScope)

    suspend fun refresh() {
        singerDetails = api.getSingerDetails(id = id)
        singerHotSongs = api.getSingerHotSong(id = id)
    }
}