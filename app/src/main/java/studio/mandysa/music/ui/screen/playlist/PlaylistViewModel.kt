package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.globalLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import studio.mandysa.fastkt.serialLiveData
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.config.cache
import studio.mandysa.music.logic.model.PlaylistInfoModel
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

    private val mPlaylistInfoModelLiveData by serialLiveData<PlaylistInfoModel>(
        key = "${id}_playlist_info_model",
        def = null,
        fastKt = cache
    )

    val playlistInfoModelLiveData: LiveData<PlaylistInfoModel> = mPlaylistInfoModelLiveData

    override fun refresh() {
        try {
            viewModelScope.launch {
                mIsRefreshingLiveData.value = true
                mSongsLiveData.value = api.getPlaylistSongs(id = id)
                mPlaylistInfoModelLiveData.value = api.getSongListInfo(id = id)
                mIsRefreshingLiveData.value = false
            }
        } catch (e: Exception) {
            POPWindows.postValue(e.message.toString())
        }
    }

    override fun isRefresh(): Boolean {
        return mPlaylistInfoModelLiveData.value == null || mSongsLiveData.value == null
    }

}