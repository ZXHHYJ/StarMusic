package studio.mandysa.music.ui.screen.me.like

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import studio.mandysa.music.logic.ktx.getPagInfo
import studio.mandysa.music.logic.model.SongModel
import studio.mandysa.music.logic.network.api

class LikePagingSource(val id: String) : PagingSource<Int, SongModel>() {
    override fun getRefreshKey(state: PagingState<Int, SongModel>): Int? = null

    private val mMateSongList by lazy {
        api.getSongListInfo(id = id).songList
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SongModel> {
        return try {
            withContext(Dispatchers.IO) {
                val pagInfo = mMateSongList.getPagInfo(params.key ?: 0, 15)
                val response = api.getSongInfo(ids = pagInfo.data)
                LoadResult.Page(
                    data = response,
                    prevKey = pagInfo.prevKey,
                    nextKey = pagInfo.nextKey
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}