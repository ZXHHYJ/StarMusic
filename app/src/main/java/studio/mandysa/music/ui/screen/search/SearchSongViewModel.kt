package studio.mandysa.music.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class SearchSongViewModel(keywords: String) : ViewModel() {
    val songSource = Pager(PagingConfig(pageSize = 15)) {
        SearchSongSource(keywords)
    }.flow.cachedIn(viewModelScope)
}