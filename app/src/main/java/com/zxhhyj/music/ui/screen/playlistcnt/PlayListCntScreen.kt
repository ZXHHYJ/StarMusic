package com.zxhhyj.music.ui.screen.playlistcnt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.logic.repository.PlayListRepository.removeSong
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.view.AppButton
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popUpTo

@Composable
fun PlayListCntScreen(
    paddingValues: PaddingValues,
    id: String,
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
) {
    val playListBean = PlayListRepository.playList.firstOrNull {
        it.uuid == id
    } ?: run {
        mainNavController.popUpTo { it == ScreenDestination.PlayList }
        return
    }
    val songs = playListBean.songs.mapNotNull { playListSongBean ->
        MediaLibHelper.songs.find {
            it.data == playListSongBean.data
        }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppTopBar(title = { Text(text = playListBean.name) }, actions = {
                AppIconButton(onClick = {
                    sheetNavController.navigate(
                        SheetDestination.PlaylistMenu(
                            playListBean
                        )
                    )
                }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert, contentDescription = null
                    )
                }
            }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppAsyncImage(
                        modifier = Modifier.size(210.dp), data = songs.firstOrNull()?.coverUrl
                    )
                    Spacer(modifier = Modifier.height(vertical))
                    Text(
                        text = playListBean.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    AppButton(onClick = { PlayerManager.play(songs, 0) }, icon = {
                        Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null)
                    }, text = {
                        Text(text = stringResource(id = R.string.play_all))
                    }, enabled = songs.isNotEmpty()
                    )
                }
            }
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            roundColumn {
                itemsIndexed(songs) { index, item ->
                    SongItem(songBean = item, sheetNavController = sheetNavController, actions = {
                        AppIconButton(onClick = { playListBean.removeSong(item) }) {
                            Icon(imageVector = Icons.Rounded.Remove, contentDescription = null)
                        }
                    }, onClick = {
                        PlayerManager.play(songs, index)
                    })
                }
            }
        }
    }
}