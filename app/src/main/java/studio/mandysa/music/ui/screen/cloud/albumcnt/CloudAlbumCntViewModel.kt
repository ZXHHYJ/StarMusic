package studio.mandysa.music.ui.screen.cloud.albumcnt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.config.api

class CloudAlbumCntViewModel(id: String) : ViewModel() {
    val albumInfo = flow {
        emit(api.getAlbumContent(id = id))
    }.asLiveData()
}