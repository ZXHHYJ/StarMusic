package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.globalLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.config.cache
import studio.mandysa.music.logic.model.PlaylistSong
import studio.mandysa.music.ui.common.POPWindows
import studio.mandysa.music.ui.common.SwipeRefreshViewModel

class PlaylistViewModel(private val id: String) : SwipeRefreshViewModel() {

    private val mSongsLiveData by globalLiveData<ArrayList<PlaylistSong>>(
        key = id,
        def = null,
        fastKt = cache
    )

    val songsLiveData: LiveData<ArrayList<PlaylistSong>> = mSongsLiveData

    val playlistInfoModel = flow {
        emit(api.getSongListInfo(id = id))
    }.asLiveData(viewModelScope.coroutineContext)


    override fun refresh() {
        viewModelScope.launch {
            mIsRefreshingLiveData.value = true
            try {
                mSongsLiveData.value = api.getPlaylistSongs(id = id)
            } catch (e: Exception) {
                POPWindows.postValue(e.message.toString())
            }
            mIsRefreshingLiveData.value = false
        }
    }

    override fun isRefresh(): Boolean {
        return mSongsLiveData.value == null
    }

}