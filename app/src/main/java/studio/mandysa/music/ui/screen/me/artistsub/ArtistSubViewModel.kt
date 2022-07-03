package studio.mandysa.music.ui.screen.me.artistsub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.network.api

class ArtistSubViewModel : ViewModel() {
    val artistSubs = flow {
        emit(api.artistSublist())
    }.asLiveData(context = viewModelScope.coroutineContext)
}