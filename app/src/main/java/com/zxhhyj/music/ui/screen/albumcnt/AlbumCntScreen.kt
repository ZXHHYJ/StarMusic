package com.zxhhyj.music.ui.screen.albumcnt

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.MediaLibRepository.songs
import com.zxhhyj.music.service.media.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun AlbumCntScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    paddingValues: PaddingValues,
    album: SongBean.Album
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = album.name,
                actions = {}
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppAsyncImage(
                        modifier = Modifier.size(210.dp),
                        data = album.songs.getOrNull(0)?.coverUrl
                    )
                    Spacer(
                        modifier = Modifier.height(vertical)
                    )
                    Text(
                        text = album.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = album.songs.first().artist.name,
                        fontSize = 14.sp,
                        color = LocalColorScheme.current.highlight,
                        modifier = Modifier.clickable {
                            mainNavController.navigate(
                                ScreenDestination.SingerCnt(
                                    album.songs.first().artist
                                )
                            )
                        }
                    )
                    AppButton(
                        onClick = { PlayManager.play(album.songs, 0) },
                        imageVector = Icons.Rounded.PlayArrow,
                        text = stringResource(id = R.string.play_all),
                        modifier = Modifier.padding(vertical = vertical / 2)
                    )
                }
            }
        }) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(album.songs) { index, item ->
                    SongItem(songBean = item, sheetNavController = sheetNavController) {
                        PlayManager.play(album.songs, index)
                    }
                }
            }
        }
    }
}