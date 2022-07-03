package studio.mandysa.music.ui.screen.search.singer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import studio.mandysa.music.logic.network.api

class SearchSingerViewModel(keywords: String) : ViewModel() {

    val singers = flow {
        emit(api.searchSinger(keywords = keywords))
    }.asLiveData(viewModelScope.coroutineContext)

}