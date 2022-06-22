package studio.mandysa.music.ui.screen.songmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.network.api

class SongMenuViewModel(val id: String) : ViewModel() {

    private val mLiked = MutableLiveData<Boolean>()

    val liked: LiveData<Boolean> = mLiked

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mLiked.postValue(api.getLikeList().indexOf(id) != -1)
        }
    }

    fun likeMusic(like: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (like == mLiked.value)
                return@launch
            if (api.likeMusic(id = id, like = like).code == 200) {
                mLiked.postValue(like)
            }
        }
    }

}