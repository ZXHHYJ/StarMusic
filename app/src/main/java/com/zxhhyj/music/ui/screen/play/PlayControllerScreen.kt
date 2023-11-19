package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.map
import com.mxalbert.sharedelements.SharedElement
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.utils.toTimeString
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.translucentWhiteColor
import com.zxhhyj.music.ui.theme.translucentWhiteFixBugColor
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.view.AppCard
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun PlayControllerScreen(sheetNavController: NavController<SheetDestination>) {
    BoxWithPercentages(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val song by PlayerManager.currentSongLiveData().observeAsState()

            SharedElement(
                key = ShareAlbumKey,
                screenKey = PlayScreenDestination.Controller,
                transitionSpec = MaterialFadeOutTransitionSpec
            ) {
                val elevation = vertical
                AppCard(
                    modifier = Modifier
                        .padding(bottom = elevation)
                        .size(100.wp),
                    backgroundColor = Color.DarkGray,
                    elevation = elevation,
                ) {
                    AppAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        data = song?.coverUrl
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.0f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = song?.songName ?: "",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = song?.artist?.name ?: "",
                        color = translucentWhiteColor,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(50))
                        .background(translucentWhiteFixBugColor)
                        .clickable {
                            song?.let {
                                sheetNavController.navigate(SheetDestination.SongPanel(it))
                            }
                        }
                )
            }

            ItemSpacer()

            val progress by PlayerManager.progressLiveData().map { it.toFloat() }.observeAsState(0f)

            val duration by PlayerManager.durationLiveData().map { it.toFloat() }.observeAsState(0f)

            val sliderHeight = 16.dp

            AppSlider(
                value = progress,
                valueRange = 0f..duration,
                onValueChange = {
                    PlayerManager.seekTo(it.toInt())
                },
                onDragStart = {
                    PlayerManager.pause()
                },
                onDragEnd = {
                    PlayerManager.start()
                },
                thumbSize = sliderHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sliderHeight),
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = translucentWhiteFixBugColor,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = progress.toInt().toTimeString(),
                    color = translucentWhiteColor,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = duration.toInt().toTimeString(),
                    color = translucentWhiteColor,
                    fontSize = 14.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.hp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val buttonSize = 18.wp

                AppIconButton(onClick = { PlayerManager.skipToPrevious() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
                        contentDescription = null,
                        modifier = Modifier.size(buttonSize),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.wp))

                val playPauseState by PlayerManager.pauseLiveData().map {
                    if (it) R.drawable.ic_play else R.drawable.ic_pause
                }.observeAsState(R.drawable.ic_play)

                AppIconButton(onClick = {
                    if (PlayerManager.pauseLiveData().value == true) {
                        PlayerManager.start()
                    } else {
                        PlayerManager.pause()
                    }
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = playPauseState),
                        contentDescription = null,
                        modifier = Modifier
                            .scale(1.2f)
                            .size(23.wp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.wp))
                AppIconButton(onClick = { PlayerManager.skipToNext() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
                        contentDescription = null,
                        modifier = Modifier.size(buttonSize),
                        tint = Color.White
                    )
                }
            }
        }
    }
}