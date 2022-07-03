package studio.mandysa.music.ui.screen.singer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import studio.mandysa.music.logic.model.SingerAlbumModel
import studio.mandysa.music.logic.network.api

class SingerAlbumPagingSource(private val id: String) : PagingSource<Int, SingerAlbumModel>() {

    override fun getRefreshKey(state: PagingState<Int, SingerAlbumModel>): Int? = null

    private val mPageDataMap = HashMap<Int, List<SingerAlbumModel>>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SingerAlbumModel> {
        return try {
            val index = params.key ?: 0
            if (mPageDataMap[index] == null) {
                mPageDataMap[index] =
                    api.getSingerAlbum(id = id, limit = params.loadSize, offset = index)
            }
            val nextPageData =
                api.getSingerAlbum(id = id, limit = params.loadSize, offset = index + 1)
            mPageDataMap[index + 1] = nextPageData
            LoadResult.Page(
                data = mPageDataMap[index]!!,
                prevKey = if (index == 0) null else index - 1,
                nextKey = if (nextPageData.isEmpty()) null else index + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}