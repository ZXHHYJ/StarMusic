package studio.mandysa.music.ui.screen.search.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class SearchSingleViewModel(keywords: String) : ViewModel() {
    val songSource = Pager(PagingConfig(pageSize = 15)) {
        SearchSingleSource(keywords)
    }.flow.cachedIn(viewModelScope)
}