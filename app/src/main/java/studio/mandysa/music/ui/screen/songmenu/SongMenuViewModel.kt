package studio.mandysa.music.ui.screen.songmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.config.api

class SongMenuViewModel(val id: String) : ViewModel() {

    private val mLikedLiveData = MutableLiveData<Boolean>()

    val likedLiveData: LiveData<Boolean> = mLikedLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mLikedLiveData.postValue(api.getLikeList().indexOf(id) != -1)
        }
    }

    fun likeMusic(like: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (like == mLikedLiveData.value)
                return@launch
            if (api.likeMusic(id = id, like = like).code == 200) {
                mLikedLiveData.postValue(like)
            }
        }
    }

}