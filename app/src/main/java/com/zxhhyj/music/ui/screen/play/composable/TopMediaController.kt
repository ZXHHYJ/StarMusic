package com.zxhhyj.music.ui.screen.play.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mxalbert.sharedelements.SharedElement
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.ktx.allArtist
import com.zxhhyj.music.service.playmanager.ktx.artist
import com.zxhhyj.music.service.playmanager.ktx.coverUrl
import com.zxhhyj.music.service.playmanager.ktx.title
import com.zxhhyj.music.ui.composable.AppAsyncImage
import com.zxhhyj.music.ui.composable.AppCard
import com.zxhhyj.music.ui.composable.AppIcon
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.play.MaterialFadeInTransitionSpec
import com.zxhhyj.music.ui.screen.play.PlayScreenDestination
import com.zxhhyj.music.ui.screen.play.ShareAlbumKey
import com.zxhhyj.music.ui.theme.*

@Composable
fun TopMediaController(
    navController: NavController<PlayScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    modifier: Modifier = Modifier,
    screenKey: PlayScreenDestination
) {
    val song by PlayManager.changeMusicLiveData().observeAsState()
    Row(
        modifier = modifier.heightIn(max = 82.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SharedElement(
            key = ShareAlbumKey,
            screenKey = screenKey,
            transitionSpec = MaterialFadeInTransitionSpec
        ) {
            AppCard(
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .padding(
                        PaddingValues.Absolute(
                            left = playScreenHorizontal,
                            right = horizontal / 2,
                            top = vertical,
                            bottom = vertical
                        )
                    )
                    .size(56.dp),
            ) {
                AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song?.coverUrl) {
                    navController.moveToTop {
                        it == PlayScreenDestination.Main
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1.0f)
                .height(46.dp),
        ) {
            Text(
                text = song?.title ?: "",
                color = Color.White,
                fontSize = 16.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = song?.artist?.allArtist() ?: "",
                color = translucentWhite,
                fontSize = 14.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        AppIcon(
            imageVector = Icons.Rounded.MoreVert,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(translucentWhiteFixBug)
                .clickable {
                    sheetNavController.navigate(BottomSheetDestination.SongMenu(song!!))
                }
        )
        Spacer(modifier = Modifier.padding(end = playScreenHorizontal))
    }
}