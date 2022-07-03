package studio.mandysa.music.ui.screen.playlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import studio.mandysa.music.logic.ktx.getPagInfo
import studio.mandysa.music.logic.model.PlaylistSong
import studio.mandysa.music.logic.network.api

class PlaylistPagingSource(private val id: String) : PagingSource<Int, PlaylistSong>() {

    override fun getRefreshKey(state: PagingState<Int, PlaylistSong>): Int? = null

    private lateinit var mMateSongList: List<PlaylistSong>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlaylistSong> {
        return try {
            if (!::mMateSongList.isInitialized) {
                mMateSongList = api.getPlaylistSongs(id = id)
            }
            val pagInfo = mMateSongList.getPagInfo(params.key ?: 0, params.loadSize)
            LoadResult.Page(
                data = pagInfo.data,
                prevKey = pagInfo.prevKey,
                nextKey = pagInfo.nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}