package studio.mandysa.music.ui.screen.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.model.PlaylistSong

class ListViewModel(val id: String) : ViewModel() {
    private val mSongsLiveData = MutableLiveData<List<PlaylistSong>>()

    val songsLiveData: LiveData<List<PlaylistSong>> = mSongsLiveData

    private val mPlaylistInfoModelLiveData = MutableLiveData<PlaylistInfoModel>()

    val playlistInfoModelLiveData: LiveData<PlaylistInfoModel> = mPlaylistInfoModelLiveData

    suspend fun refresh() {
        mPlaylistInfoModelLiveData.value = api.network().getSongListInfo(id = id)
        mSongsLiveData.value = api.network().getPlaylistSongs(id = id)
    }
}