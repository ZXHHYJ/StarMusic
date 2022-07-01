package studio.mandysa.music.ui.screen.search.singer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class SearchSingerViewModel(keywords: String) : ViewModel() {

    val singers = flow {
        emit(api.searchSinger(keywords = keywords))
    }.flowOn(Dispatchers.IO)

}