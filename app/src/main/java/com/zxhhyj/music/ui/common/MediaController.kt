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
import androidx.compose.material.Icon
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
import com.zxhhyj.music.logic.utils.coverUrl
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.ui.Card
import com.zxhhyj.ui.Divider
import com.zxhhyj.ui.LocalColorScheme

@Composable
fun MediaController(panelState: PanelState?, modifier: Modifier, onClick: () -> Unit) {
    val controlBarHeight = 50.dp
    val coverLength = 56.dp
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(LocalColorScheme.current.background)
        ) {
            Divider(modifier = Modifier.fillMaxWidth())
        }
        Box(modifier = Modifier.padding(horizontal = horizontal)) {
            Box {
                val song by PlayManager.currentSongLiveData().observeAsState()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(controlBarHeight)
                        .align(Alignment.BottomCenter),
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Transparent,
                ) {
                    ImageMotionBlur(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(controlBarHeight)
                            .clickable(onClick = onClick)
                            .background(Color.Gray),
                        imageUrl = song?.album?.coverUrl,
                        paused = panelState != PanelState.COLLAPSED
                    )
                    Row(
                        modifier = Modifier
                            .padding(start = coverLength)
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
                        Icon(
                            imageVector = ImageVector.vectorResource(playPauseState),
                            tint = Color.White,
                            contentDescription = null,
                            modifier = Modifier
                                .size(buttonSize)
                                .clickable {
                                    if (PlayManager.pauseLiveData().value == true) {
                                        PlayManager.start()
                                    } else {
                                        PlayManager.pause()
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
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
                Card(
                    backgroundColor = Color.Transparent,
                    modifier = Modifier.size(coverLength)
                ) {
                    AppAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        data = song?.album?.coverUrl ?: ""
                    )
                }
            }
        }
    }
}