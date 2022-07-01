package studio.mandysa.music.ui.screen.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.model.BannerModel
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendSong
import studio.mandysa.music.logic.network.api
import studio.mandysa.music.ui.base.BaseViewModel
import studio.mandysa.music.ui.common.State

class BrowseViewModel : BaseViewModel() {

    private val mBannersLiveData = MutableLiveData<List<BannerModel>>()

    val bannersLiveData: LiveData<List<BannerModel>> = mBannersLiveData

    private val mRecommendSongLiveData = MutableLiveData<List<RecommendSong>>()

    val recommendSongLiveData: LiveData<List<RecommendSong>> = mRecommendSongLiveData

    private val mRecommendPlaylistLiveData = MutableLiveData<List<PlaylistModel>>()

    val recommendPlaylistLiveData: LiveData<List<PlaylistModel>> = mRecommendPlaylistLiveData

    private val mPlaylistSquareLiveData = MutableLiveData<List<PlaylistModel>>()

    val playlistSquareLiveData: LiveData<List<PlaylistModel>> = mPlaylistSquareLiveData

    override fun loading() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mBannersLiveData.postValue(api.getBannerList())
                mRecommendSongLiveData.postValue(api.getRecommendSong())
                mRecommendPlaylistLiveData.postValue(api.getRecommendPlaylist())
                mPlaylistSquareLiveData.postValue(api.getPlaylistSquare())
                stateLiveData.postValue(State.CONTENT)
            } catch (e: Exception) {
                stateLiveData.postValue(State.ERROR)
            }
        }
    }

    override fun refresh() {
        loading()
    }

}