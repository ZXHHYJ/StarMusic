package com.zxhhyj.music.ui.screen.play.queue

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.service.playmanager.ktx.allArtist
import com.zxhhyj.music.service.playmanager.ktx.artist
import com.zxhhyj.music.service.playmanager.ktx.title
import com.zxhhyj.music.ui.composable.AppCard
import com.zxhhyj.music.ui.composable.AppIcon
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.play.PlayScreenDestination
import com.zxhhyj.music.ui.screen.play.composable.TopMediaController
import com.zxhhyj.music.ui.theme.playScreenHorizontal
import com.zxhhyj.music.ui.theme.translucentWhite
import com.zxhhyj.music.ui.theme.vertical

@Composable
fun PlayQueueScreen(
    navController: NavController<PlayScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>
) {
    val playlist by PlayManager.changePlayListLiveData().observeAsState()
    playlist?.let {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                item {
                    TopMediaController(
                        navController = navController,
                        sheetNavController = sheetNavController,
                        screenKey = PlayScreenDestination.PlayQueue
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxSize()
            ) {
                itemsIndexed(it) { index, model ->
                    QueueSongItem(
                        model
                    ) {
                        PlayManager.play(it, index)
                    }
                }
            }
        }
    }
}

@Composable
private fun QueueSongItem(
    song: SongBean,
    onClick: () -> Unit
) {
    AppCard(backgroundColor = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(start = playScreenHorizontal)
                    .padding(vertical = vertical),
            ) {
                Text(
                    text = song.title,
                    color = Color.White,
                    fontSize = 15.sp, maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = song.artist.allArtist(),
                    color = translucentWhite,
                    fontSize = 13.sp, maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
            AppIcon(
                imageVector = Icons.Rounded.Remove,
                tint = translucentWhite,
                contentDescription = null,
                modifier = Modifier
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.padding(end = playScreenHorizontal))
        }
    }
}