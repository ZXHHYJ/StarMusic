package studio.mandysa.music.ui.screen.search.singer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import studio.mandysa.music.logic.model.SearchSingerModel
import studio.mandysa.music.logic.network.api

class SearchSingerSource(private val keywords: String) : PagingSource<Int, SearchSingerModel>() {
    override fun getRefreshKey(state: PagingState<Int, SearchSingerModel>): Int? {
        return null
    }

    private val mPageDataMap = HashMap<Int, List<SearchSingerModel>>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchSingerModel> {
        return try {
            withContext(Dispatchers.IO) {
                val index = params.key ?: 0
                if (mPageDataMap[index] == null) {
                    mPageDataMap[index] =
                        api.searchSinger(
                            keywords = keywords,
                            limit = params.loadSize,
                            offset = index * params.loadSize
                        )
                }
                val nextPageData =
                    api.searchSinger(
                        keywords = keywords,
                        limit = params.loadSize,
                        offset = (index + 1) * params.loadSize
                    )
                mPageDataMap[index + 1] = nextPageData
                LoadResult.Page(
                    data = mPageDataMap[index]!!,
                    prevKey = if (index == 0) null else index - 1,
                    nextKey = if (nextPageData.isEmpty()) null else index + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}