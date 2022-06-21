package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun PlayQueueScreen() {
    val playlist by PlayManager.changePlayListLiveData().observeAsState(listOf())
    LazyColumn {
        itemsIndexed(playlist) { index, model ->
            SongItem(
                index,
                title = model.title,
                singer = model.artist.allArtist()
            ) {
                PlayManager.loadPlaylist(playlist, index)
            }
        }
    }
}

@Composable
private fun SongItem(
    position: Int,
    title: String,
    singer: String, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(verticalMargin)
                .size(50.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$position",
                fontSize = 16.sp,
                color = translucentWhite,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = verticalMargin),
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp, maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = singer,
                color = translucentWhite,
                fontSize = 13.sp, maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            modifier = Modifier.padding(end = horizontalMargin),
            tint = translucentWhite
        )
    }
}