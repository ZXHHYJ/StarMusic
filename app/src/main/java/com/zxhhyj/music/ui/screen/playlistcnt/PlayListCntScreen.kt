package com.zxhhyj.music.ui.screen.playlistcnt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.view.AppButton
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun PlayListCntScreen(
    playlist: PlayListModel,
    sheetNavController: NavController<SheetDestination>,
    paddingValues: PaddingValues
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = playlist.name,
                actions = {
                    AppIconButton(onClick = {
                        sheetNavController.navigate(
                            SheetDestination.PlaylistMenu(
                                playlist
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppAsyncImage(
                        modifier = Modifier.size(210.dp),
                        data = playlist.songs.firstOrNull()?.coverUrl
                    )
                    Spacer(modifier = Modifier.height(vertical))
                    Text(text = playlist.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    AppButton(
                        onClick = { PlayManager.play(playlist.songs, 0) },
                        imageVector = Icons.Rounded.PlayArrow,
                        text = stringResource(id = R.string.play_all),
                        modifier = Modifier.padding(vertical = vertical / 2)
                    )
                }
            }
        }) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(playlist.songs) { index, item ->
                    SongItem(
                        songBean = item,
                        sheetNavController = sheetNavController,
                        actions = {
                            AppIconButton(onClick = { playlist.removeSong(item) }) {
                                Icon(imageVector = Icons.Rounded.Remove, contentDescription = null)
                            }
                        },
                        onClick = {
                            PlayManager.play(playlist.songs, index)
                        })
                }
            }
        }
    }
}