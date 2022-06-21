package studio.mandysa.music.ui.screen.singer.singeralbum

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import studio.mandysa.music.logic.ktx.getPagInfo
import studio.mandysa.music.logic.model.SongModel
import studio.mandysa.music.logic.network.api

class SingerAlbumPagingSource(private val id: String) : PagingSource<Int, SongModel>() {

    override fun getRefreshKey(state: PagingState<Int, SongModel>): Int? = null

    private val mSongsList by lazy {
        api.getSingerAllSong(id = id)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SongModel> {
        return try {
            withContext(Dispatchers.IO) {
                val pagInfo = mSongsList.getPagInfo(params.key ?: 0, 50)
                LoadResult.Page(
                    data = pagInfo.data,
                    prevKey = pagInfo.prevKey,
                    nextKey = pagInfo.nextKey
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}