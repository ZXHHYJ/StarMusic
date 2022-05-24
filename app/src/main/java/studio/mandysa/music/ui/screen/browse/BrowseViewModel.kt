package studio.mandysa.music.ui.screen.browse

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.network.ServiceCreator

class BrowseViewModel : ViewModel() {

    val banners = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getBannerList())
    }.flowOn(Dispatchers.IO)

    val recommendSongs = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getRecommendedSong())
    }.flowOn(Dispatchers.IO)

    val recommendPlaylist = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getRecommendPlaylist())
    }.flowOn(Dispatchers.IO)

    val playlistSquare = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getPlaylistSquare())
    }.flowOn(Dispatchers.IO)

}