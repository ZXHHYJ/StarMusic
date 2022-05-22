package studio.mandysa.music.ui.screen.playlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.network.ServiceCreator

class PlaylistPagingSource(
    private val id: String
) :
    PagingSource<Int, MusicModel>() {

    override fun getRefreshKey(state: PagingState<Int, MusicModel>): Int? {
        return null
    }

    private val mLoadNumber = 15

    private lateinit var mateSongList: List<PlaylistInfoModel.SongList>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MusicModel> {
        return try {
            withContext(Dispatchers.IO) {
                if (!::mateSongList.isInitialized) {
                    mateSongList = ServiceCreator.create(NeteaseCloudMusicApi::class.java)
                        .getSongListInfo(id = id).songList!!
                }
                val nextPage = params.key ?: 1
                val list = ArrayList<PlaylistInfoModel.SongList>()
                val difference = mLoadNumber * nextPage
                for (i in difference until if (mateSongList.size - difference < mLoadNumber)
                    difference + kotlin.math.abs(mateSongList.size - difference)
                else
                    mLoadNumber * (nextPage + 1)) {
                    list.add(mateSongList[i])
                }
                val response =
                    ServiceCreator.create(NeteaseCloudMusicApi::class.java).getMusicInfo(list)
                LoadResult.Page(
                    data = response,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (difference > mateSongList.size) null else nextPage + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}