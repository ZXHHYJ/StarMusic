package studio.mandysa.music.ui.screen.album

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppMediumTopAppBar
import studio.mandysa.music.ui.common.AppMenuButton
import studio.mandysa.music.ui.common.AppScaffold
import studio.mandysa.music.ui.common.Preview
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColorLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    viewModel: AlbumViewModel = viewModel(factory = viewModelFactory {
        addInitializer(AlbumViewModel::class) { AlbumViewModel(id) }
    })
) {
    val albumInfo = viewModel.albumContent
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarState()
    )
    AppScaffold(modifier = Modifier
        .statusBarsPadding()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppMediumTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.album))
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) {
        Preview(modifier = Modifier.padding(it), refresh = { viewModel.refresh() }) {
            LazyColumn {
                item {
                    ContentColumnItem(
                        dialogNavController = dialogNavController,
                        coverUrl = albumInfo?.picUrl,
                        title = albumInfo?.name,
                        message = albumInfo?.description
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = horizontalMargin)
                                .padding(bottom = 5.dp)
                        ) {
                            AppMenuButton(
                                modifier = Modifier.weight(1.0f),
                                title = stringResource(id = R.string.play_all),
                                icon = Icons.Rounded.PlayArrow,
                                enabled = albumInfo?.songList != null
                            ) {
                                PlayManager.play(albumInfo!!.songList, 0)
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            AppMenuButton(
                                modifier = Modifier.weight(1.0f),
                                title = stringResource(id = R.string.more),
                                icon = Icons.Rounded.MoreVert
                            ) {
                                // TODO: 专辑菜单
                            }
                        }
                    }
                }
                albumInfo?.let {
                    itemsIndexed(it.songList) { index, item ->
                        SongItem(dialogNavController = dialogNavController, song = item) {
                            PlayManager.play(it.songList, index)
                        }
                    }
                }
                albumInfo?.company?.let {
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = horizontalMargin),
                            text = stringResource(id = R.string.issued) + ":" + it,
                            color = textColorLight,
                            fontSize = 14.sp
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}