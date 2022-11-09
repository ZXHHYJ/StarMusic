package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.service.playmanager.bean.MetaMusic
import studio.mandysa.music.service.playmanager.bean.Song
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.theme.*


@Composable
fun SongItem(
    dialogNavController: NavController<DialogDestination>,
    bean: Song.LocalBean,
    onClick: () -> Unit
) {
    SongItem(
        "content://media/external/audio/albumart/${bean.albumId}".toUri(),
        bean.songName,
        bean.artist,
        { },
        onClick
    )
}

@Composable
fun SongItem(
    dialogNavController: NavController<DialogDestination>,
    song: MetaMusic<*, *>,
    onClick: () -> Unit
) {
    SongItem(
        song.coverUrl,
        song.title,
        song.artist.allArtist(),
        { dialogNavController.navigate(DialogDestination.SongMenu(song)) },
        onClick
    )
}

@Composable
private fun SongItem(
    coverUrl: Any,
    title: String,
    artists: String,
    moreVertOnClink: () -> Unit,
    onClick: () -> Unit
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
                .padding(horizontal = horizontalMargin, vertical = verticalMargin)
                .size(50.dp), contentAlignment = Alignment.Center
        ) {
            AppAsyncImage(size = 50.dp, any = coverUrl)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = verticalMargin),
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = artists,
                color = textColorLight,
                fontSize = 13.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            tint = onBackground,
            contentDescription = null,
            modifier = Modifier
                .clickable(onClick = moreVertOnClink)
        )
        Spacer(modifier = Modifier.padding(end = horizontalMargin))
    }
}