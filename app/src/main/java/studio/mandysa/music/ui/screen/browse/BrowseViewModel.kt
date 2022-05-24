package studio.mandysa.music.ui.screen.browse

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import studio.mandysa.music.logic.model.BannerModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendSong
import studio.mandysa.music.logic.network.ServiceCreator

class BrowseViewModel : ViewModel() {

    val banners: Flow<List<BannerModel>> = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getBannerList())
    }.flowOn(Dispatchers.IO)

    val recommendSongs: Flow<List<RecommendSong>> = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getRecommendedSong())
    }.flowOn(Dispatchers.IO)

    val recommendPlaylist: Flow<List<PlaylistModel>> = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getRecommendPlaylist())
    }.flowOn(Dispatchers.IO)

    val playlistSquare: Flow<List<PlaylistModel>> = flow {
        emit(ServiceCreator.create(NeteaseCloudMusicApi::class.java).getPlaylistSquare())
    }.flowOn(Dispatchers.IO)

}