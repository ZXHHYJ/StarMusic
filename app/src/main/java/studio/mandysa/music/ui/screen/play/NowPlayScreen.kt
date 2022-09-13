package studio.mandysa.music.ui.screen.play

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.SeekBar
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.theme.*

@Composable
fun NowPlayScreen(dialogNavController: NavController<DialogDestination>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlbumCover()
        TitleAndArtist(dialogNavController)
        MusicProgressBar()
        MusicControlBar()
    }
}


@Composable
private fun AlbumCover() {
    Card(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .widthIn(max = maxWidth)
            .heightIn(max = maxWidth),
        elevation = 10.dp,
        shape = roundedCornerShape
    ) {
        AppAsyncImage(
            modifier = Modifier.size(maxWidth),
            url = PlayManager.selectMusic?.coverUrl
        )
    }
}

@Composable
private fun TitleAndArtist(dialogNavController: NavController<DialogDestination>) {
    Row(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth()
            .padding(vertical = verticalMargin), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1.0f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = PlayManager.selectMusic?.title ?: "",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.padding(top = 2.dp))
            Text(
                text = PlayManager.selectMusic?.artist?.get(0)?.name ?: "",
                color = translucentWhite,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
        Icon(
            Icons.Rounded.MoreVert, null,
            Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(translucentWhiteFixBug)
                .clickable {
                    dialogNavController.navigate(DialogDestination.SongMenu(PlayManager.selectMusic!!))
                },
            tint = Color.White
        )
    }
}

@Composable
private fun MusicProgressBar() {
    fun Int.toTime(): String {
        val s: Int = this / 1000
        return (s / 60).toString() + ":" + s % 60
    }
    SeekBar(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth(),
        value = PlayManager.progress ?: 0,
        maxValue = PlayManager.duration ?: 0,
        onValueChange = { PlayManager.seekTo(it) }
    )
    Row(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth()
    ) {
        Text(text = (PlayManager.progress ?: 0).toTime(), color = translucentWhite)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = (PlayManager.duration ?: 0).toTime(), color = translucentWhite)
    }
}

@Composable
private fun MusicControlBar() {
    val smallButtonSize = if (isMedium) 55.dp else 65.dp
    val middleButtonSize = if (isMedium) 65.dp else 85.dp

    Row(
        modifier = Modifier
            .widthIn(max = maxWidth)
            .fillMaxWidth()
            .padding(vertical = if (isMedium) 15.dp else 50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            ImageVector.vectorResource(id = R.drawable.ic_skip_previous), null,
            Modifier
                .size(smallButtonSize)
                .clip(RoundedCornerShape(smallButtonSize))
                .clickable {
                    PlayManager.skipToPrevious()
                },
            tint = Color.White
        )
        Box(modifier = Modifier.padding(horizontal = 35.dp)) {
            Icon(
                ImageVector.vectorResource(
                    id = if (PlayManager.pause != false) R.drawable.ic_play else R.drawable.ic_pause
                ),
                null,
                Modifier
                    .size(middleButtonSize)
                    .clip(RoundedCornerShape(middleButtonSize))
                    .clickable {
                        if (PlayManager.pause != false)
                            PlayManager.play()
                        else PlayManager.pause()
                    },
                tint = Color.White
            )
        }
        Icon(
            ImageVector.vectorResource(id = R.drawable.ic_skip_next), null,
            Modifier
                .size(smallButtonSize)
                .clip(RoundedCornerShape(smallButtonSize))
                .clickable {
                    PlayManager.skipToNext()
                },
            tint = Color.White
        )
    }
}