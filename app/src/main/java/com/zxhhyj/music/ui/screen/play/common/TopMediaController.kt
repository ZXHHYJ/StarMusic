package com.zxhhyj.music.ui.screen.play.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mxalbert.sharedelements.SharedElement
import com.zxhhyj.music.logic.utils.coverUrl
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.play.MaterialFadeInTransitionSpec
import com.zxhhyj.music.ui.screen.play.PlayScreen
import com.zxhhyj.music.ui.screen.play.PlayScreenDestination
import com.zxhhyj.music.ui.screen.play.ShareAlbumKey
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.translucentWhiteColor
import com.zxhhyj.music.ui.theme.translucentWhiteFixBugColor
import com.zxhhyj.music.ui.theme.vertical
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun TopMediaController(
    modifier: Modifier = Modifier,
    navController: NavController<PlayScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
) {
    val song by PlayManager.currentSongLiveData().observeAsState()
    Row(
        modifier = modifier
            .heightIn(max = 82.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(PlayScreen.PlayScreenContentHorizontal))
        SharedElement(
            key = ShareAlbumKey,
            screenKey = PlayScreenDestination.PlayQueue,
            transitionSpec = MaterialFadeInTransitionSpec
        ) {
            AppAsyncImage(
                modifier = Modifier
                    .padding(
                        PaddingValues.Absolute(
                            left = 0.dp,
                            right = horizontal / 2,
                            top = vertical,
                            bottom = vertical
                        )
                    )
                    .size(56.dp),
                data = song?.album?.coverUrl
            ) {
                navController.moveToTop {
                    it == PlayScreenDestination.Controller
                }
            }

        }
        Column(
            modifier = Modifier
                .weight(1.0f)
                .height(46.dp),
        ) {
            Text(
                text = song?.songName ?: "",
                color = Color.White,
                fontSize = 16.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = song?.artist?.name ?: "",
                color = translucentWhiteColor,
                fontSize = 14.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(translucentWhiteFixBugColor)
                .clickable {
                    sheetNavController.navigate(BottomSheetDestination.SongMenu(song!!))
                }
        )
        Spacer(modifier = Modifier.width(PlayScreen.PlayScreenContentHorizontal))
    }
}