package studio.mandysa.music.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.model.BannerModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.network.ServiceCreator

class BrowseViewModel : ViewModel() {

    private val mIsRefreshLiveData = MutableLiveData(false)

    private val mBanners = MutableLiveData(listOf<BannerModel>())

    fun getBanners(): LiveData<List<BannerModel>> = mBanners

    fun isRefresh(): LiveData<Boolean> {
        return mIsRefreshLiveData
    }

    fun refresh() {
        mIsRefreshLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            ServiceCreator.create(NeteaseCloudMusicApi::class.java).let {
                mBanners.postValue(it.getBannerList())
            }
        }
        mIsRefreshLiveData.value = false
    }
}