package com.zxhhyj.music.ui.screen.play

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.map
import com.github.krottv.compose.sliders.SliderValueHorizontal
import com.mxalbert.sharedelements.SharedElement
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.ktx.toTime
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.AppCard
import com.zxhhyj.music.ui.common.AppIcon
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.theme.translucentWhite
import com.zxhhyj.music.ui.theme.translucentWhiteFixBug
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlin.math.roundToInt

@Composable
fun NowPlayScreen(sheetNavController: NavController<BottomSheetDestination>) {
    val song by PlayManager.changeMusicLiveData().observeAsState()
    BoxWithPercentages(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (100.hp > 100.wp) {
                //歌曲专辑封面
                SharedElement(
                    key = ShareAlbumKey,
                    screenKey = PlayScreenDestination.Main,
                    transitionSpec = MaterialFadeOutTransitionSpec
                ) {
                    AppCard(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .size(100.wp),
                        backgroundColor = Color.LightGray,
                        elevation = 10.dp,
                    ) {
                        AppAsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            url = song?.album?.coverUrl ?: ""
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                //歌曲标题、歌手名称和菜单按钮
                Column(
                    modifier = Modifier
                        .weight(1.0f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = song?.songName ?: "",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.padding(top = 2.dp))
                    Text(
                        text = song?.artist?.name ?: "",
                        color = translucentWhite,
                        fontSize = 16.sp,
                        maxLines = 1
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
            }


            val progress by PlayManager.progressLiveData().map {
                it
            }.observeAsState(0)

            val duration by PlayManager.durationLiveData().map {
                it
            }.observeAsState(1)

            //歌曲进度条
            SliderValueHorizontal(
                value = progress.toFloat() / duration.toFloat(),
                onValueChange = {
                    PlayManager.seekTo((duration * it).roundToInt())
                    PlayManager.pause()
                },
                onValueChangeFinished = {
                    PlayManager.play()
                },
                thumbSizeInDp = DpSize(10.dp, 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                thumbHeightMax = true,
                track = { _: Modifier,
                          fraction: Float,
                          _: MutableInteractionSource,
                          _: List<Float>,
                          _: Boolean ->
                    LinearProgressIndicator(
                        progress = fraction,
                        color = Color.White,
                        backgroundColor = translucentWhiteFixBug,
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )
                },
                thumb = { modifier: Modifier,
                          _: Dp,
                          _: MutableInteractionSource,
                          _: Boolean,
                          thumbSize: DpSize ->
                    Spacer(
                        modifier = modifier
                            .size(thumbSize)
                            .background(Color.White, RoundedCornerShape(50))
                    )
                }
            )
            //当前歌曲播放进度和歌曲时长
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = progress.toTime(), color = translucentWhite)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = duration.toTime(), color = translucentWhite)
            }

            //上一曲、暂停和下一曲
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.hp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val buttonSize = 18.wp

                val playPauseState by PlayManager.pauseLiveData().map {
                    if (it) R.drawable.ic_play else R.drawable.ic_pause
                }.observeAsState(R.drawable.ic_play)

                AppIcon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
                    contentDescription = null,
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                            PlayManager.skipToPrevious()
                        },
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.wp))
                AppIcon(
                    imageVector = ImageVector.vectorResource(id = playPauseState),
                    contentDescription = null,
                    modifier = Modifier
                        .scale(1.2f)
                        .size(23.wp)
                        .clickable {
                            if (PlayManager.isPaused)
                                PlayManager.play()
                            else PlayManager.pause()
                        },
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.wp))
                AppIcon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
                    contentDescription = null,
                    modifier = Modifier
                        .size(buttonSize)
                        .clickable {
                            PlayManager.skipToNext()
                        },
                    tint = Color.White
                )
            }
        }
    }
}