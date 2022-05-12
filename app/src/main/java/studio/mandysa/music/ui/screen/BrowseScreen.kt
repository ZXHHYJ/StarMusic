package studio.mandysa.music.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.common.ItemSubTitleScreen
import studio.mandysa.music.ui.common.ItemTitle
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.viewmodel.BrowseViewModel
import studio.mandysa.music.ui.viewmodel.EventViewModel


@Composable
private fun BannerItem(typeTitle: String, bannerUrl: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        )
        {
            AsyncImage(
                model = bannerUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Text(
                    text = typeTitle,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("SetTextI18n")
@Composable
fun BrowseScreen(
    event: EventViewModel = viewModel(),
    viewModel: BrowseViewModel = viewModel()
) {
    val isRefreshing by viewModel.isRefresh().observeAsState(true)
    val bannerItems by viewModel.getBanners().observeAsState(arrayListOf())
    val recommendSongs by viewModel.getRecommendSongs().observeAsState(arrayListOf())
    val cookie = event.getCookieLiveData().observeAsState()
    if (cookie.value != null) {
        viewModel.initialize(cookie.value!!)
        viewModel.refresh()
    }
    SwipeRefresh(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refresh() },
    ) {
        LazyColumn {
            item {
                ItemTitle(stringResource(R.string.browse))
            }
            item {
                HorizontalPager(count = bannerItems.size) {
                    val model = bannerItems[it]
                    BannerItem(typeTitle = model.typeTitle, bannerUrl = model.pic) {
                        when (model.targetType) {
                            3000 -> {

                            }
                            1000 -> {

                            }
                            1 -> {

                            }
                            else -> {

                            }
                        }
                    }
                }
            }
            item {
                ItemSubTitleScreen(stringResource(R.string.recommend_playlist))
            }
            item {
                ItemSubTitleScreen(stringResource(R.string.recommend_song))
            }
            items(recommendSongs.size) {
                val model = recommendSongs[it]
                SongItem(
                    position = it + 1,
                    title = model.title,
                    singer = model.artist.allArtist()
                ) {

                }
            }
        }
    }
    /*AndroidViewBinding(FragmentBrowseBinding::inflate) {
        browseRecycle.linear().setup {
            addViewBinding<TitleModel, ItemTitleBinding> { holder ->
                titleTv.text = holder.model.title
            }
            addCompose<SubTitleModel> {
                ItemSubTitleScreen(model.title)
            }
            addCompose<RecommendSongs.RecommendSong> {
                SongItem(
                    position = modelPosition + 1,
                    title = model.title,
                    singer = model.artist.allArtist()
                ) {
                    PlayManager.loadPlaylist(
                        models,
                        modelPosition
                    )
                }
            }
            addCompose<BannerModels> {
                Linear(
                    orientation = RecyclerView.HORIZONTAL,
                    init = { PagerSnapHelper().attachToRecyclerView(this) },
                    models = model.list!!
                ) {
                    addCompose<BannerModels.BannerModel> {
                        BannerItem(typeTitle = model.typeTitle, bannerUrl = model.pic) {
                            when (model.targetType) {
                                3000 -> {
                                    *//* val uri: Uri = Uri.parse(model.url)
                                     val intent = Intent(Intent.ACTION_VIEW, uri)
                                     startActivity(intent)*//*
                                }
                                1000 -> {
                                    *//* it.findNavController().navigate(
                                         BrowseFragmentDirections.actionHomeFragmentToPlaylistFragment(
                                             model.encodeId
                                         )
                                     )*//*
                                }
                                1 -> {

                                }
                                else -> {

                                }
                            }
                        }
                    }
                }
            }
            addCompose<PlaylistSquareModel> {
                Linear(
                    orientation = RecyclerView.HORIZONTAL,
                    models = model.playlist!!,
                    init = {
                        addItemDecoration(HorizontalAlbumItemDecoration())
                        LinearSnapHelper().attachToRecyclerView(this)
                    }) {
                    addCompose<PlaylistModel> {
                        PlaylistItem(title = model.name, coverUrl = model.picUrl) {
                            *//*  it.findNavController().navigate(
                                  BrowseFragmentDirections.actionHomeFragmentToPlaylistFragment(
                                      model.id
                                  )
                              )*//*
                        }
                    }
                }
            }
            addCompose<RecommendPlaylist> {
                Linear(
                    orientation = RecyclerView.HORIZONTAL,
                    models = model.playlist!!,
                    init = {
                        addItemDecoration(HorizontalAlbumItemDecoration())
                    }) {
                    addCompose<PlaylistModel> {
                        PlaylistItem(title = model.name, coverUrl = model.picUrl) {
                            *//* it.findNavController().navigate(
                                 BrowseFragmentDirections.actionHomeFragmentToPlaylistFragment(
                                     model.id
                                 )
                             )*//*
                        }
                    }
                }
            }
        }
        browseStateLayout.showLoading {
            *//*val mCookie = mEvent.getCookieLiveData().observeAsState()
            mEvent.getCookieLiveData().observe(viewLifecycleOwner) { cookie ->
                if (cookie == null) return@observe
                viewLifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.IO) {
                    try {
                        ServiceCreator.create(NeteaseCloudMusicApi::class.java).let {
                            val bannerModel = it.getBannerList()
                            val recommendPlaylist =
                                it.getRecommendPlaylist(cookie)
                            val playlistSquare = it.getPlaylistSquare()
                            val recommendedSong = it.getRecommendedSong(cookie)
                            withContext(Dispatchers.Main) {
                                browseRecycle.recyclerAdapter.headers = listOf(
                                    TitleModel(context.getString(R.string.browse)),
                                    bannerModel,
                                    SubTitleModel(getString(R.string.recommend_playlist)),
                                    recommendPlaylist,
                                    SubTitleModel(getString(R.string.playlist_square)),
                                    playlistSquare,
                                    SubTitleModel(getString(R.string.recommend_song))
                                )
                                browseRecycle.recyclerAdapter.models = recommendedSong.list!!
                                browseStateLayout.showContentState()
                            }
                        }
                    } catch (e: AnnaException) {
                        browseStateLayout.showErrorState()
                    }
                }
            }*//*
        }
        browseStateLayout.showError {
            browseRecycle.recyclerAdapter.clearModels()
        }
        browseStateLayout.showLoadingState()
    }*/
}