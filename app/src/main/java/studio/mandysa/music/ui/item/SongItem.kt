package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.service.playmanager.ktx.artist
import studio.mandysa.music.service.playmanager.ktx.coverUrl
import studio.mandysa.music.service.playmanager.ktx.title
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.common.AppIcon
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.theme.*



@Composable
fun SongItem(
    sheetNavController: NavController<BottomSheetDestination>,
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
            AppCard(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .padding(horizontal = horizontal, vertical = vertical)
                    .size(50.dp),
            ) {
                AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song.coverUrl)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(vertical = vertical),
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
            AppIcon(
                imageVector = Icons.Rounded.MoreVert,
                tint = onBackground,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        sheetNavController.navigate(BottomSheetDestination.SongMenu(song))
                    }
            )
            Spacer(modifier = Modifier.padding(end = horizontal))
        }
    }
}