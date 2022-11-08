package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import studio.mandysa.music.logic.bean.LocalMusicBean
import studio.mandysa.music.service.playmanager.bean.MetaMusic
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.onBackground
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight
import studio.mandysa.music.ui.theme.verticalMargin


@Composable
fun SongItem(
    dialogNavController: NavController<DialogDestination>,
    bean: LocalMusicBean,
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

            AppAsyncImage(
                size = 50.dp,
                any = "content://media/external/audio/albumart/${bean.albumId}".toUri()
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = verticalMargin),
        ) {
            Text(
                text = bean.songName,
                color = textColor,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = bean.artist,
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
                .clickable {
                    //dialogNavController.navigate(DialogDestination.SongMenu(song))
                }
        )
        Spacer(modifier = Modifier.padding(end = horizontalMargin))
    }
}

@Composable
fun SongItem(
    dialogNavController: NavController<DialogDestination>,
    song: MetaMusic<*, *>,
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
            AppAsyncImage(size = 50.dp, any = song.coverUrl)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = verticalMargin),
        ) {
            Text(
                text = song.title,
                color = textColor,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = song.artist.allArtist(),
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
                .clickable {
                    dialogNavController.navigate(DialogDestination.SongMenu(song))
                }
        )
        Spacer(modifier = Modifier.padding(end = horizontalMargin))
    }
}