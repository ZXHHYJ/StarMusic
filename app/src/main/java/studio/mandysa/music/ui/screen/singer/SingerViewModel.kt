package studio.mandysa.music.ui.screen.singer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class SingerViewModel(id: String) : ViewModel() {
    val singerInfo = flow {
        emit(api.getSingerDetails(id = id))
    }.flowOn(Dispatchers.IO).asLiveData()
}