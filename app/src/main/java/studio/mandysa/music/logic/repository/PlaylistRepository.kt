package studio.mandysa.music.logic.repository

import com.drake.serialize.serialize.serialLiveData
import studio.mandysa.music.logic.bean.PlaylistInfoBean
import studio.mandysa.music.logic.bean.PlaylistSongBean

class PlaylistRepository(id: String) {

    val songsLiveData by serialLiveData<ArrayList<PlaylistSongBean>?>(name = "playlist${id}songs")

    val infoLiveData by serialLiveData<PlaylistInfoBean?>(name = "playlist${id}info")
}