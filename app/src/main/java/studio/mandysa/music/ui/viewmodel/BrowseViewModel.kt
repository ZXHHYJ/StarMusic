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
import studio.mandysa.music.logic.model.RecommendSong
import studio.mandysa.music.logic.network.ServiceCreator

class BrowseViewModel : ViewModel() {

    sealed class Status {
        data class Banners(val value: List<BannerModel>) : Status()
        data class RecommendSongs(val value: List<RecommendSong>) : Status()
        data class Error(val e: AnnaException) : Status()
    }

    private lateinit var mCookie: String

    private val mIsRefreshLiveData = MutableLiveData(false)

    private val mBanners = MutableLiveData<List<BannerModel>>()

    private val mRecommendSongs = MutableLiveData<List<RecommendSong>>()

    fun getBanners(): LiveData<List<BannerModel>> = mBanners

    fun getRecommendSongs(): LiveData<List<RecommendSong>> = mRecommendSongs

    fun isRefresh(): LiveData<Boolean> {
        return mIsRefreshLiveData
    }

    fun initialize(cookie: String) {
        mCookie = cookie;
    }

    fun refresh() {
        mIsRefreshLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ServiceCreator.create(NeteaseCloudMusicApi::class.java).let {
                    mBanners.postValue(it.getBannerList())
                    mRecommendSongs.postValue(it.getRecommendedSong(mCookie))
                }
            } catch (e: AnnaException) {

            }
        }
        mIsRefreshLiveData.value = false
    }
}