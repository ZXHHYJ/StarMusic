package com.zxhhyj.music.ui.screen.playlistcnt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.logic.utils.coverUrl
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.AppButton
import com.zxhhyj.music.ui.common.AppCard
import com.zxhhyj.music.ui.common.AppRoundIcon
import com.zxhhyj.music.ui.common.AppScaffold
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.common.stateprompt.StatePrompt
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.theme.vertical
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun PlayListCntScreen(
    playlist: PlayListModel,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = playlist.name,
                actions = {
                    AppRoundIcon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            sheetNavController.navigate(BottomSheetDestination.PlaylistMenu(playlist))
                        })
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppCard(backgroundColor = Color.Transparent) {
                        AppAsyncImage(
                            url = playlist.songs.firstOrNull()?.album?.coverUrl,
                            modifier = Modifier.size(210.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(vertical))
                    Text(text = playlist.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    AppButton(
                        onClick = { PlayManager.play(playlist.songs.toList(), 0) },
                        imageVector = Icons.Rounded.PlayArrow,
                        text = stringResource(id = R.string.play_all),
                        modifier = Modifier.padding(vertical = vertical / 2)
                    )
                }
            }
        }) {
        StatePrompt(empty = playlist.songs.isEmpty(), modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(playlist.songs) { index, item ->
                    SongItem(
                        song = item,
                        sheetNavController = sheetNavController,
                        actions = {
                            AppRoundIcon(
                                imageVector = Icons.Rounded.Remove,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        playlist.removeSong(item)
                                    }
                            )
                        },
                        onClick = {
                            PlayManager.play(playlist.songs.toList(), index)
                        })
                }
            }
        }
    }
}