package studio.mandysa.music.ui.screen.me.ilike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class ILikeViewModel : ViewModel() {
    val songs = Pager(PagingConfig(15)) {
        ILikePagingSource()
    }.flow.cachedIn(viewModelScope)
}