package studio.mandysa.music.logic.repository

import com.drake.serialize.serialize.serialLiveData
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.model.PlaylistSong

class PlaylistRepository(id: String) {

    val songsLiveData by serialLiveData<ArrayList<PlaylistSong>?>(name = "playlist${id}songs")

    val infoLiveData by serialLiveData<PlaylistInfoModel?>(name = "playlist${id}info")

}