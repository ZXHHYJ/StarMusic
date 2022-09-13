package studio.mandysa.music.ui.screen.controller

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.KenBurns
import studio.mandysa.music.ui.common.PanelState
import studio.mandysa.music.ui.theme.anyBarColor
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.roundedCornerShape

@Composable
fun ControllerScreen(panelState: PanelState?, onClick: () -> Unit) {
    val controlBarHeight = 50.dp
    val coverSize = 55.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(anyBarColor)
        ) {
        }
        Box(modifier = Modifier.padding(horizontal = horizontalMargin)) {
            Box {
                val coverUrl = PlayManager.selectMusic?.coverUrl ?: ""
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(controlBarHeight)
                        .align(Alignment.BottomCenter),
                    contentColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                ) {
                    Box {
                        KenBurns(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .height(controlBarHeight)
                                .clickable(onClick = onClick)
                                .background(Color.Gray),
                            imageUrl = coverUrl,
                            paused = PlayManager.pause ?: false
                        )
                        Row(
                            modifier = Modifier
                                .padding(start = coverSize)
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1.0f),
                                text = PlayManager.selectMusic?.title ?: "",
                                fontSize = 16.sp, maxLines = 1,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(if (PlayManager.pause != false) R.drawable.ic_play else R.drawable.ic_pause),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(controlBarHeight)
                                    .padding(8.dp)
                                    .clip(roundedCornerShape)
                                    .clickable {
                                        if (PlayManager.pause != false) {
                                            PlayManager.play()
                                        } else {
                                            PlayManager.pause()
                                        }
                                    }
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_skip_next),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(controlBarHeight)
                                    .padding(8.dp)
                                    .clip(roundedCornerShape)
                                    .clickable {
                                        PlayManager.skipToNext()
                                    }
                            )
                        }
                    }
                }
                AppAsyncImage(modifier = Modifier.size(coverSize), url = coverUrl)
            }
        }
    }
}