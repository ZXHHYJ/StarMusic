package studio.mandysa.music.ui.screen.me.meplaylist

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.logic.model.UserPlaylist
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MePlaylistScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    mePlaylistViewModel: MePlaylistViewModel = viewModel(),
) {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PlaylistItem(userPlaylist: UserPlaylist) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .combinedClickable(onClick = {
                            mainNavController.navigate(ScreenDestination.Playlist(userPlaylist.id))
                        }, onLongClick = {
                            dialogNavController.navigate(DialogDestination.PlaylistMenu(userPlaylist.id))
                        }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppAsyncImage(size = 100.dp, url = userPlaylist.coverImgUrl)
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(text = userPlaylist.name, fontSize = 15.sp, color = textColor)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = userPlaylist.nickname, fontSize = 14.sp, color = textColor)
                    }
                }
            }
        }
    }

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
                    Text(text = "我的歌单")
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) { it ->
        val list by remember { mePlaylistViewModel.getMePlaylistListState() }
        Preview(refresh = { mePlaylistViewModel.refresh() }) {
            AppLazyVerticalGrid(
                modifier = Modifier
                    .padding(it),
                contentPadding = PaddingValues(horizontal = horizontalMargin),
                horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                list?.let {
                    adaptiveItems(it.size) { index ->
                        //第一个是我喜欢的歌单
                        PlaylistItem(userPlaylist = it[index])
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}