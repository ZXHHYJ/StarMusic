package studio.mandysa.music.ui.screen.playlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import studio.mandysa.music.logic.ktx.getPagInfo
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.network.api

class PlaylistPagingSource(private val id: String) : PagingSource<Int, MusicModel>() {

    override fun getRefreshKey(state: PagingState<Int, MusicModel>): Int? = null

    private lateinit var mMateSongList: List<PlaylistInfoModel.SongList>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MusicModel> {
        return try {
            if (!::mMateSongList.isInitialized) {
                mMateSongList = api.getSongListInfo(id = id).songList
            }
            val pagInfo = mMateSongList.getPagInfo(params.key ?: 0, params.loadSize)
            val response = api.getSongInfo(ids = pagInfo.data)
            LoadResult.Page(
                data = response,
                prevKey = pagInfo.prevKey,
                nextKey = pagInfo.nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}