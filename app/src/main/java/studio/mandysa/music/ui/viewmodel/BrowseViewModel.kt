package studio.mandysa.music.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mandysax.anna2.exception.AnnaException
import studio.mandysa.music.logic.model.BannerModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendSong
import studio.mandysa.music.logic.network.ServiceCreator

class BrowseViewModel : ViewModel() {

    private lateinit var mCookie: String

    private val mBanners = MutableLiveData<List<BannerModel>>()

    private val mRecommendSongs = MutableLiveData<List<RecommendSong>>()

    private val mRecommendPlaylist = MutableLiveData<List<PlaylistModel>>()

    private val mPlaylistSquare = MutableLiveData<List<PlaylistModel>>()

    fun getBanners(): LiveData<List<BannerModel>> = mBanners

    fun getRecommendSongs(): LiveData<List<RecommendSong>> = mRecommendSongs

    fun getRecommendPlaylist(): LiveData<List<PlaylistModel>> = mRecommendPlaylist

    fun getPlaylistSquare(): LiveData<List<PlaylistModel>> = mPlaylistSquare

    fun initialize(cookie: String) {
        mCookie = cookie
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ServiceCreator.create(NeteaseCloudMusicApi::class.java).let {
                    mBanners.postValue(it.getBannerList())
                    mRecommendSongs.postValue(it.getRecommendedSong(mCookie))
                    mRecommendPlaylist.postValue(it.getRecommendPlaylist(mCookie))
                    mPlaylistSquare.postValue(it.getPlaylistSquare())
                }
            } catch (e: AnnaException) {

            }
        }
    }
}