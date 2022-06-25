package studio.mandysa.music.ui.screen.me.artistsub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class ArtistSubViewModel : ViewModel() {
    val artistSubs = flow {
        emit(api.artistSublist())
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)
}