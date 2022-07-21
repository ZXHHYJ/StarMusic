package studio.mandysa.music.ui.screen.me.meplaylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.logic.model.UserPlaylist
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppDivider
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.onBackground
import studio.mandysa.music.ui.theme.round
import studio.mandysa.music.ui.theme.textColor

@Composable
fun MePlaylistScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    mePlaylistViewModel: MePlaylistViewModel = viewModel(),
) {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    fun PlaylistItem(userPlaylist: UserPlaylist) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalMargin)
                .padding(top = 5.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .combinedClickable(onClick = {
                        mainNavController.navigate(ScreenDestination.Playlist(userPlaylist.id))
                    }, onLongClick = {
                        dialogNavController.navigate(DialogDestination.PlaylistMenu(userPlaylist.id))
                    }),
                shape = RoundedCornerShape(round)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppAsyncImage(size = 100.dp, url = userPlaylist.coverImgUrl)
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(text = userPlaylist.name, fontSize = 16.sp, color = textColor)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = userPlaylist.nickname, fontSize = 14.sp, color = textColor)
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = onBackground,
            elevation = 0.dp
        ) {
            IconButton(onClick = { mainNavController.pop() }) {
                Icon(Icons.Rounded.ArrowBack, null)
            }
        }
        AppDivider()
        val list by mePlaylistViewModel.meAllPlaylist.observeAsState()
        LazyColumn {
            list?.let {
                items(it) { playlist ->
                    PlaylistItem(userPlaylist = playlist)
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}