package studio.mandysa.music.ui.screen.search.single

import androidx.paging.PagingSource
import androidx.paging.PagingState
import studio.mandysa.music.logic.bean.SearchSongBean
import studio.mandysa.music.logic.config.api

class SearchSingleSource(private val keywords: String) : PagingSource<Int, SearchSongBean>() {
    override fun getRefreshKey(state: PagingState<Int, SearchSongBean>): Int? {
        return null
    }

    private val mPageDataMap = HashMap<Int, List<SearchSongBean>>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchSongBean> {
        return try {
            val index = params.key ?: 0
            if (mPageDataMap[index] == null) {
                mPageDataMap[index] =
                    api.searchMusic(
                        keywords = keywords,
                        limit = params.loadSize,
                        offset = index * params.loadSize
                    )
            }
            val nextPageData =
                api.searchMusic(
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
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}