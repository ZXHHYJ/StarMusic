package com.zxhhyj.music.ui.screen.albumcnt

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun AlbumCntScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    paddingValues: PaddingValues,
    albumName: String
) {
    val songs = MediaLibHelper.Album[albumName]
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppTopBar(
                title = { Text(text = albumName) },
                actions = {}
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppAsyncImage(
                        modifier = Modifier.size(210.dp),
                        data = songs.getOrNull(0)?.coverUrl
                    )
                    Spacer(
                        modifier = Modifier.height(vertical)
                    )
                    Text(
                        text = albumName,
                        fontSize = 16.sp,
                        color = LocalColorScheme.current.text,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = songs.first().artistName!!,
                        fontSize = 14.sp,
                        color = LocalColorScheme.current.highlight,
                        modifier = Modifier.clickable {
                            mainNavController.navigate(ScreenDestination.SingerCnt(songs.first().artistName!!))
                        }
                    )
                    AppButton(
                        onClick = { PlayerManager.play(songs, 0) },
                        icon = {
                            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null)
                        },
                        text = {
                            Text(text = stringResource(id = R.string.play_all))
                        }
                    )
                }
            }
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            roundColumn {
                itemsIndexed(songs) { index, item ->
                    SongItem(songBean = item, sheetNavController = sheetNavController) {
                        PlayerManager.play(songs, index)
                    }
                }
            }
        }
    }
}