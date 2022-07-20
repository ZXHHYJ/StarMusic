package studio.mandysa.music.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class SwipeRefreshViewModel : ViewModel() {
    val isRefreshing = MutableLiveData(false)
    abstract suspend fun preview()
    abstract suspend fun refresh()
}