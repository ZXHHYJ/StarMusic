package studio.mandysa.music.ui.screen.me.meplaylist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.UserPlaylist
import studio.mandysa.music.logic.network.ServiceCreator

class MePlaylistViewModel : ViewModel() {
    val playlist: Flow<List<UserPlaylist>> = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getUserPlaylist().apply {
            val list = this as ArrayList<UserPlaylist>
            list.removeAt(0)
        })
    }.flowOn(Dispatchers.IO)
}