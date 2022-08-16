package studio.mandysa.music.ui.screen.search.singer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.SearchSingerModel

class SearchSingerViewModel(private val keywords: String) : ViewModel() {

    var singers by mutableStateOf<List<SearchSingerModel>?>(null)
        private set

    suspend fun refresh() {
        singers = api.searchSinger(keywords = keywords)
    }

}