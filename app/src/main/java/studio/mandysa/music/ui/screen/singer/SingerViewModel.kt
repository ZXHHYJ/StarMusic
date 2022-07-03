package studio.mandysa.music.ui.screen.singer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.network.api

class SingerViewModel(id: String) : ViewModel() {
    val singerInfo = flow {
        emit(api.getSingerDetails(id = id))
    }.asLiveData(viewModelScope.coroutineContext)
}