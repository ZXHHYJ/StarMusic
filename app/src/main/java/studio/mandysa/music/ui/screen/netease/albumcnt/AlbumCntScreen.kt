package studio.mandysa.music.ui.screen.netease.albumcnt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.onBackground
import studio.mandysa.music.ui.theme.textColorLight

@Composable
fun AlbumCntScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    albumCntViewModel: AlbumCntViewModel = viewModel(factory = viewModelFactory {
        addInitializer(AlbumCntViewModel::class) { AlbumCntViewModel(id) }
    })
) {
    val albumInfo by albumCntViewModel.albumInfo.observeAsState()
    Column {
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
                        MenuItem(
                            modifier = Modifier.weight(1.0f),
                            title = stringResource(id = R.string.play_all),
                            imageVector = Icons.Rounded.PlayArrow,
                            enabled = albumInfo?.songList != null
                        ) {
                            //PlayManager.play(albumInfo!!.songList, 0)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        MenuItem(
                            modifier = Modifier.weight(1.0f),
                            title = stringResource(id = R.string.more),
                            imageVector = Icons.Rounded.MoreVert
                        ) {
                            // TODO: 专辑菜单
                        }
                    }
                }
            }
            albumInfo?.let {
                itemsIndexed(it.songList) { index, item ->
                    /*SongItem(dialogNavController = dialogNavController, song = item) {
                        PlayManager.play(it.songList, index)
                    }*/
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