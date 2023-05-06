package com.zxhhyj.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.map
import com.zxhhyj.music.R
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.ktx.coverUrl
import com.zxhhyj.music.ui.common.image.AppAsyncImage
import com.zxhhyj.music.ui.theme.appBackgroundColor
import com.zxhhyj.music.ui.theme.horizontal

@Composable
fun MediaController(panelState: PanelState?, modifier: Modifier, onClick: () -> Unit) {
    val controlBarHeight = 50.dp
    val coverSize = 55.dp
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(appBackgroundColor)
        ) {
            AppDivider(modifier = Modifier.fillMaxWidth())
        }
        Box(modifier = Modifier.padding(horizontal = horizontal)) {
            Box {
                val song by PlayManager.changeMusicLiveData().observeAsState()
                AppCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(controlBarHeight)
                        .align(Alignment.BottomCenter),
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Transparent,
                ) {
                    MotionBlur(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(controlBarHeight)
                            .clickable(onClick = onClick)
                            .background(Color.Gray),
                        url = song?.album?.coverUrl,
                        paused = panelState != PanelState.COLLAPSED
                    )
                    Row(
                        modifier = Modifier
                            .padding(start = coverSize)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = horizontal)
                                .weight(1.0f),
                            text = song?.songName ?: "",
                            fontSize = 16.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        val playPauseState by PlayManager.pauseLiveData().map {
                            if (it) R.drawable.ic_play else R.drawable.ic_pause
                        }.observeAsState(R.drawable.ic_play)
                        val buttonSize = 36.dp
                        AppRoundIcon(
                            imageVector = ImageVector.vectorResource(playPauseState),
                            tint = Color.White,
                            contentDescription = null,
                            modifier = Modifier
                                .size(buttonSize)
                                .clickable {
                                    if (PlayManager.isPaused) {
                                        PlayManager.play()
                                    } else {
                                        PlayManager.pause()
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AppRoundIcon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_skip_next),
                            tint = Color.White,
                            contentDescription = null,
                            modifier = Modifier
                                .size(buttonSize)
                                .clickable {
                                    PlayManager.skipToNext()
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                AppCard(backgroundColor = Color.Transparent, modifier = Modifier.size(coverSize)) {
                    AppAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        url = song?.album?.coverUrl ?: ""
                    )
                }
            }
        }
    }
}