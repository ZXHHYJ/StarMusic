package studio.mandysa.music.ui.screen.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class AlbumViewModel(id: String) : ViewModel() {
    val albumInfo = flow {
        emit(api.getAlbumContent(id = id))
    }.flowOn(Dispatchers.IO).asLiveData()
}