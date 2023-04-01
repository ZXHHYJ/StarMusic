package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
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
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.service.playmanager.ktx.artist
import studio.mandysa.music.service.playmanager.ktx.title
import studio.mandysa.music.ui.common.AppIcon
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.theme.vertical
import studio.mandysa.music.ui.theme.playScreenHorizontal
import studio.mandysa.music.ui.theme.playScreenMaxWidth
import studio.mandysa.music.ui.theme.translucentWhite

/**
 * @author 黄浩
 */

@Composable
fun PlayQueueScreen(sheetNavController: NavController<BottomSheetDestination>) {
    val playlist by PlayManager.changePlayListLiveData().observeAsState(listOf())
    LazyColumn(
        modifier = Modifier
            .widthIn(max = playScreenMaxWidth)
            .fillMaxSize()
    ) {
        itemsIndexed(playlist) { index, model ->
            SongItem(
                sheetNavController,
                index,
                model
            ) {
                PlayManager.play(playlist, index)
            }
        }
    }
}

@Composable
private fun SongItem(
    sheetNavController: NavController<BottomSheetDestination>,
    position: Int,
    song: SongBean,
    onClick: () -> Unit
) {
    AppCard(backgroundColor = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = playScreenHorizontal, end = vertical)
                    .size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${position + 1}",
                    fontSize = 16.sp,
                    color = translucentWhite,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
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
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        sheetNavController.navigate(BottomSheetDestination.SongMenu(song))
                    },
                tint = translucentWhite
            )
            Spacer(modifier = Modifier.padding(end = playScreenHorizontal))
        }
    }
}