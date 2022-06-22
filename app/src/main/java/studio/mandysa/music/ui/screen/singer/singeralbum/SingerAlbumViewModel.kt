package studio.mandysa.music.ui.screen.singer.singeralbum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.network.api

class SingerAlbumViewModel(id: String) : ViewModel() {

    /*   val albums = Pager(PagingConfig(pageSize = 15)) {
           SingerAlbumPagingSource(id)
       }.flow.cachedIn(viewModelScope)*/

    init {
        viewModelScope.launch(Dispatchers.IO) {
            api.getSingerAlbum(id = id, limit = 30, offset = 0)
        }
    }

}