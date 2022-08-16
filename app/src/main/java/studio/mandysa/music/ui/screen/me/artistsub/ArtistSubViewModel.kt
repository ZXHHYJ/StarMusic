package studio.mandysa.music.ui.screen.me.artistsub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.ArtistSubModel

class ArtistSubViewModel : ViewModel() {
    var artistSublist by mutableStateOf<List<ArtistSubModel>?>(null)
        private set

    suspend fun refresh() {
        artistSublist = api.artistSublist()
    }
}