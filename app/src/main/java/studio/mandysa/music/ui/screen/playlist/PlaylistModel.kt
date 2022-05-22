package studio.mandysa.music.ui.screen.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mandysax.anna2.exception.AnnaException
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.network.ServiceCreator

class PlaylistModel(private val id: String) : ViewModel() {

    private val mPlaylistInfo = MutableLiveData<PlaylistInfoModel>()

    fun getPlaylistInfo(): LiveData<PlaylistInfoModel> = mPlaylistInfo

    val songs = Pager(PagingConfig(pageSize = 15)) {
        PlaylistPagingSource(id)
    }.flow.cachedIn(viewModelScope)

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ServiceCreator.create(NeteaseCloudMusicApi::class.java).let {
                    mPlaylistInfo.postValue(it.getSongListInfo(id = id))
                }
            } catch (e: AnnaException) {

            }
        }
    }
}