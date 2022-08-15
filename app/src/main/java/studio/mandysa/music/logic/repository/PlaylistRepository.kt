package studio.mandysa.music.logic.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.syncSerialLiveData
import studio.mandysa.fastkt.serialLiveData
import studio.mandysa.music.logic.config.cacheFastKt
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.model.PlaylistSong

class PlaylistRepository(viewModel: ViewModel, id: String) {

    val songsLiveData by viewModel.syncSerialLiveData<ArrayList<PlaylistSong>>(
        "playlist${id}songs",
        null,
        cacheFastKt
    )

    val infoLiveData by serialLiveData<PlaylistInfoModel>(
        "playlist${id}info",
        null,
        cacheFastKt
    )

    companion object {

        fun ViewModel.playlistRepository(id: String): PlaylistRepository {
            return PlaylistRepository(this, id)
        }

    }
}