package com.zxhhyj.music.ui.screen.singercnt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository.songs
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCard
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController

@Composable
fun SingerCntScreen(
    sheetNavController: NavController<BottomSheetDestination>,
    paddingValues: PaddingValues,
    artist: SongBean.Artist
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColorScheme.current.background)
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = artist.name,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontal)
                        .padding(bottom = vertical)
                ) {
                    Text(
                        text = artist.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalColorScheme.current.highlight,
                        modifier = Modifier.weight(1.0f)
                    )
                    AppCard(
                        backgroundColor = LocalColorScheme.current.highlight,
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint = LocalColorScheme.current.onText,
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
        RoundColumn(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(artist.songs) { index, item ->
                    SongItem(sheetNavController = sheetNavController, song = item) {
                        PlayManager.play(artist.songs, index)
                    }
                }
            }
        }
    }
}
