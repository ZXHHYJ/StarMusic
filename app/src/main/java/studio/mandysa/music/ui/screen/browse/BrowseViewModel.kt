package studio.mandysa.music.ui.screen.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.globalLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.config.cache
import studio.mandysa.music.logic.model.BannerModel
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendSong
import studio.mandysa.music.ui.common.POPWindows
import studio.mandysa.music.ui.common.SwipeRefreshViewModel

class BrowseViewModel : SwipeRefreshViewModel() {

    private val mBannersLiveData by globalLiveData<ArrayList<BannerModel>>(
        key = "banners",
        def = null,
        fastKt = cache
    )

    val bannersLiveData: LiveData<ArrayList<BannerModel>> = mBannersLiveData

    private val mRecommendSongLiveData by globalLiveData<ArrayList<RecommendSong>>(
        key = "recommend_song",
        def = null,
        fastKt = cache
    )

    val recommendSongLiveData: LiveData<ArrayList<RecommendSong>> = mRecommendSongLiveData

    private val mRecommendPlaylistLiveData by globalLiveData<ArrayList<PlaylistModel>>(
        key = "recommend_playlist",
        def = null,
        fastKt = cache
    )

    val recommendPlaylistLiveData: LiveData<ArrayList<PlaylistModel>> = mRecommendPlaylistLiveData

    private val mPlaylistSquareLiveData by globalLiveData<ArrayList<PlaylistModel>>(
        key = "playlist_square",
        def = null,
        fastKt = cache
    )

    val playlistSquareLiveData: LiveData<ArrayList<PlaylistModel>> = mPlaylistSquareLiveData

    override fun refresh() {
        try {
            viewModelScope.launch {
                mIsRefreshingLiveData.value = true
                mBannersLiveData.value = api.getBannerList()
                mRecommendSongLiveData.value = api.getRecommendSong()
                mRecommendPlaylistLiveData.value = api.getRecommendPlaylist()
                mPlaylistSquareLiveData.value = api.getPlaylistSquare()
                mIsRefreshingLiveData.value = false
            }
        } catch (e: Exception) {
            POPWindows.postValue(e.message.toString())
        }
    }

    override fun isRefresh(): Boolean {
        return bannersLiveData.value == null || recommendPlaylistLiveData.value == null || recommendSongLiveData.value == null || playlistSquareLiveData.value == null
    }

}