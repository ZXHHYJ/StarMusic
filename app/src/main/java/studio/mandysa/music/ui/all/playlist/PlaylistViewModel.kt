package studio.mandysa.music.ui.all.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mandysax.anna2.exception.AnnaException
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.network.ServiceCreator
import kotlin.math.abs

class PlaylistViewModel : ViewModel() {

    sealed class Status {
        data class Header(val value: PlaylistInfoModel) : Status()
        data class Next(val value: List<MusicModel>) : Status()
        data class Error(val e: AnnaException) : Status()
    }

    private val mLoadNumber = 15

    private val mIndex = MutableLiveData(0)

    private val mStatusLiveData = MutableLiveData<Status>()

    private lateinit var mSongList: List<PlaylistInfoModel.SongList>

    fun bind(cookie: String, id: String): LiveData<Status> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val model = ServiceCreator.create(NeteaseCloudMusicApi::class.java)
                    .getSongListInfo(cookie, id)
                mStatusLiveData.postValue(Status.Header(model))
                mSongList = model.songList!!
                viewModelScope.launch(Dispatchers.Main) {
                    mIndex.observeForever {
                        val list = ArrayList<PlaylistInfoModel.SongList>()
                        val difference = mLoadNumber * it
                        if (difference > mSongList.size)
                            return@observeForever
                        for (i in difference until if (mSongList.size - difference < mLoadNumber)
                            difference + abs(mSongList.size - difference)
                        else
                            mLoadNumber * (it + 1)) {
                            list.add(mSongList[i])
                        }
                        viewModelScope.launch(Dispatchers.IO) {
                            try {
                                mStatusLiveData.postValue(
                                    Status.Next(
                                        ServiceCreator.create(NeteaseCloudMusicApi::class.java)
                                            .getMusicInfo(list)
                                    )
                                )
                            } catch (e: AnnaException) {
                                mStatusLiveData.postValue(Status.Error(e))
                            }
                        }
                    }
                }
            } catch (e: AnnaException) {
                e.printStackTrace()
                mStatusLiveData.postValue(Status.Error(e))
            }
        }
        return mStatusLiveData
    }

    fun nextPage() {
        mIndex.value = mIndex.value!! + 1
    }
}