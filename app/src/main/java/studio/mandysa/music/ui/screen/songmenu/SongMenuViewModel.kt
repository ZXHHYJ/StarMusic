package studio.mandysa.music.ui.screen.songmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.network.api

class SongMenuViewModel(val id: String) : ViewModel() {

    val isLike = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            isLike.postValue(api.getLikeList().indexOf(id) != -1)
        }
    }

    fun likeMusic(like: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (like == isLike.value)
                return@launch
            if (api.likeMusic(id = id, like = like).code == 200) {
                isLike.postValue(like)
            }
        }
    }

}