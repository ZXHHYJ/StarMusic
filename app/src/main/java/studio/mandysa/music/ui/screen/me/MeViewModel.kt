package studio.mandysa.music.ui.screen.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.network.api

class MeViewModel : ViewModel() {

    val userInfo = flow {
        emit(api.getUserInfo())
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)

    //获取用户的所以歌单，*网易云把我喜欢也算在里面
    val allPlaylist = flow {
        emit(api.getUserPlaylist())
    }.flowOn(Dispatchers.IO).asLiveData(context = viewModelScope.coroutineContext)

}