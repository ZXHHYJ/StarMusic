package studio.mandysa.music.ui.screen.album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.AlbumInfoModel

class AlbumViewModel(val id: String) : ViewModel() {

    var albumContent by mutableStateOf<AlbumInfoModel?>(null)
        private set

    suspend fun refresh() {
        albumContent = api.getAlbumContent(id = id)
    }
}