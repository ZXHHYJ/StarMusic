package studio.mandysa.music.ui.screen.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.model.PlaylistSong
import studio.mandysa.music.ui.common.SwipeRefreshViewModel

class ListViewModel(val id: String) : SwipeRefreshViewModel() {
    private val mSongsLiveData = MutableLiveData<List<PlaylistSong>>()

    val songsLiveData: LiveData<List<PlaylistSong>> = mSongsLiveData

    private val mPlaylistInfoModelLiveData = MutableLiveData<PlaylistInfoModel>()

    val playlistInfoModelLiveData: LiveData<PlaylistInfoModel> = mPlaylistInfoModelLiveData

    override suspend fun preview() {
        mPlaylistInfoModelLiveData.value = api.shortCache().getSongListInfo(id = id)
        mSongsLiveData.value = api.shortCache().getPlaylistSongs(id = id)
    }

    override suspend fun refresh() {
        mPlaylistInfoModelLiveData.value = api.network().getSongListInfo(id = id)
        mSongsLiveData.value = api.network().getPlaylistSongs(id = id)
    }
}