package studio.mandysa.music.ui.screen.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.network.ServiceCreator

class MeViewModel : ViewModel() {
    private val mUserInfo = MutableLiveData<UserModel>()

    fun getUserInfo(): LiveData<UserModel> = mUserInfo

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            ServiceCreator.create(NeteaseCloudMusicApi::class.java).let {
                mUserInfo.postValue(it.getUserInfo(timestamp = System.currentTimeMillis()))
            }
        }
    }
}