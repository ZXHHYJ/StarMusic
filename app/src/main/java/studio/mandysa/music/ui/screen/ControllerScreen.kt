package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.map
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.RoundAsyncImage
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.navHeight

@Composable
fun ControllerScreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(navHeight)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(start = horizontalMargin), verticalAlignment = Alignment.CenterVertically
    ) {
        val coverUrl by PlayManager.changeMusicLiveData().map { return@map it.coverUrl }
            .observeAsState("")
        RoundAsyncImage(size = 48.dp, url = coverUrl)
        val title by PlayManager.changeMusicLiveData().map { return@map it.title }
            .observeAsState("")
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1.0f),
            text = title,
            fontSize = 16.sp, maxLines = 1,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        val playPauseState by PlayManager.pauseLiveData().map {
            if (it) R.drawable.ic_play else R.drawable.ic_pause
        }.observeAsState(R.drawable.ic_play)
        Icon(
            painter = painterResource(playPauseState),
            contentDescription = null,
            modifier = Modifier
                .width(55.dp)
                .padding(10.dp)
                .fillMaxHeight()
                .clickable {
                    if (PlayManager.pauseLiveData().value == true)
                        PlayManager.play()
                    else PlayManager.pause()
                }
        )
        Icon(
            painter = painterResource(R.drawable.ic_skip_next),
            contentDescription = null,
            modifier = Modifier
                .width(55.dp)
                .padding(10.dp)
                .fillMaxHeight()
                .clickable {
                    PlayManager.skipToNext()
                }
        )
    }
}