package com.zxhhyj.music.ui.screen.singercnt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository.songs
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.ui.common.AppRoundCard
import com.zxhhyj.music.ui.common.AppRoundIcon
import com.zxhhyj.music.ui.common.AppScaffold
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.theme.appAccentColor
import com.zxhhyj.music.ui.theme.appTextAccentColor
import com.zxhhyj.music.ui.theme.onTextColor
import com.zxhhyj.music.ui.theme.vertical
import dev.olshevski.navigation.reimagined.NavController

@Composable
fun SingerCntScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues,
    artist: SongBean.Artist
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = artist.name,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = vertical)
                ) {
                    Text(
                        text = artist.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = appTextAccentColor,
                        modifier = Modifier.weight(1.0f)
                    )
                    AppRoundCard(backgroundColor = appAccentColor) {
                        AppRoundIcon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint = onTextColor,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    PlayManager.play(artist.songs, 0)
                                }
                        )
                    }
                }
            }
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(artist.songs) { index, item ->
                SongItem(sheetNavController = sheetNavController, song = item) {
                    PlayManager.play(artist.songs, index)
                }
            }
        }
    }
}
