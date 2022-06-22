package studio.mandysa.music.ui.screen.singer.singeralbum

import androidx.paging.PagingSource
import androidx.paging.PagingState
import studio.mandysa.music.logic.model.MusicModel

class SingerAlbumPagingSource(private val id: String) : PagingSource<Int, MusicModel>() {

    override fun getRefreshKey(state: PagingState<Int, MusicModel>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MusicModel> {
        /*return try {
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
        }*/
        return TODO()
    }
}