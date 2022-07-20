package studio.mandysa.music.ui.screen.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.model.BannerModel
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendSong
import studio.mandysa.music.ui.common.SwipeRefreshViewModel

class BrowseViewModel : SwipeRefreshViewModel() {

    private val mBannersLiveData = MutableLiveData<List<BannerModel>>()

    val bannersLiveData: LiveData<List<BannerModel>> = mBannersLiveData

    private val mRecommendSongLiveData = MutableLiveData<List<RecommendSong>>()

    val recommendSongLiveData: LiveData<List<RecommendSong>> = mRecommendSongLiveData

    private val mRecommendPlaylistLiveData = MutableLiveData<List<PlaylistModel>>()

    val recommendPlaylistLiveData: LiveData<List<PlaylistModel>> = mRecommendPlaylistLiveData

    private val mPlaylistSquareLiveData = MutableLiveData<List<PlaylistModel>>()

    val playlistSquareLiveData: LiveData<List<PlaylistModel>> = mPlaylistSquareLiveData

    override suspend fun preview() {
        mBannersLiveData.value = api.cache().getBannerList()
        mRecommendSongLiveData.value = api.cache().getRecommendSong()
        mRecommendPlaylistLiveData.value = api.cache().getRecommendPlaylist()
        mPlaylistSquareLiveData.value = api.cache().getPlaylistSquare()
    }

    override suspend fun refresh() {
        mBannersLiveData.value = api.network().getBannerList()
        mRecommendSongLiveData.value = api.network().getRecommendSong()
        mRecommendPlaylistLiveData.value = api.network().getRecommendPlaylist()
        mPlaylistSquareLiveData.value = api.network().getPlaylistSquare()
    }

}