package studio.mandysa.music.ui.screen.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.network.api

class MeViewModel : ViewModel() {
    private val mUserInfo = MutableLiveData<UserModel>()

    fun getUserInfo(): LiveData<UserModel> = mUserInfo

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            mUserInfo.postValue(api.getUserInfo(timestamp = System.currentTimeMillis()))
        }
    }
}