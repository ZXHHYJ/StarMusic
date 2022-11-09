package studio.mandysa.music.ui.screen.search.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.config.api

class SearchSingleViewModel(keywords: String) : ViewModel() {
    init {
        viewModelScope.launch {
            println(api.searchMusic(keywords = keywords, limit = 0, offset = 30))
        }
    }
}