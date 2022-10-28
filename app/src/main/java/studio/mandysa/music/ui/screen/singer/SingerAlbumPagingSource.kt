package studio.mandysa.music.ui.screen.singer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import studio.mandysa.music.logic.bean.SingerAlbumBean
import studio.mandysa.music.logic.config.api

class SingerAlbumPagingSource(private val id: String) : PagingSource<Int, SingerAlbumBean>() {

    override fun getRefreshKey(state: PagingState<Int, SingerAlbumBean>): Int? = null

    private val mPageDataMap = HashMap<Int, List<SingerAlbumBean>>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SingerAlbumBean> {
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