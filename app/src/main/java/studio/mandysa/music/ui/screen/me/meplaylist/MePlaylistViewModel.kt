package studio.mandysa.music.ui.screen.me.meplaylist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.UserPlaylist

class MePlaylistViewModel : ViewModel() {

    private var mMePlaylistList = mutableStateOf<List<UserPlaylist>?>(null)

    /**
     * 获取用户所有歌单
     */
    fun getMePlaylistListState() = mMePlaylistList

    suspend fun refresh() {
        mMePlaylistList.value = api.getUserPlaylist()
    }

}