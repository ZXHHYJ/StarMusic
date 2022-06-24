package studio.mandysa.music.ui.screen.search.singer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class SearchSingerViewModel(keywords: String) : ViewModel() {

    val singerSource = Pager(PagingConfig(15)) {
        SearchSingerSource(keywords)
    }.flow.cachedIn(viewModelScope)

}