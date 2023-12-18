package com.zxhhyj.music.ui.screen.lyricconfig

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AlbumMotionBlur
import com.zxhhyj.music.ui.common.lyric.Lyric
import com.zxhhyj.music.ui.theme.translucentWhiteColor
import com.zxhhyj.ui.theme.StarDimens
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun LyricConfigScreen(paddingValues: PaddingValues) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.lyric)) })
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        val currentSong by PlayerManager.currentSongFlow.collectAsState()
                        AlbumMotionBlur(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.DarkGray),
                            albumUrl = currentSong?.coverUrl,
                            paused = false
                        )
                        //这里参数与播放界面的歌词要保持一致
                        val liveTime by PlayerManager.progressFlow.collectAsState()
                        val animDurationMillis = 1000
                        Lyric(
                            modifier = Modifier
                                .fillMaxSize(),
                            lyric = currentSong?.lyric,
                            liveTime = liveTime,
                            translation = SettingRepository.EnableLyricsTranslation,
                            lyricScrollAnimationSpec = tween(animDurationMillis),
                            lyricItem = { modifier: Modifier,
                                          model: String,
                                          index: Int,
                                          position: Int ->
                                val textColor by animateColorAsState(
                                    targetValue = if (position == index) Color.White else translucentWhiteColor,
                                    label = "lyric_text_color",
                                    animationSpec = tween(animDurationMillis)
                                )
                                val textScale by animateFloatAsState(
                                    targetValue = if (position == index) 1.05f else 1f,
                                    label = "lyric_text_scale",
                                    animationSpec = tween(animDurationMillis)
                                )
                                Text(
                                    modifier = modifier
                                        .padding(
                                            vertical = 18.dp,
                                            horizontal = StarDimens.horizontal
                                        )
                                        .graphicsLayer {
                                            scaleX = textScale
                                            scaleY = textScale
                                            transformOrigin = TransformOrigin(0f, 0f)
                                        },
                                    text = model,
                                    color = textColor,
                                    fontSize = SettingRepository.LyricFontSize.sp,
                                    fontWeight = if (SettingRepository.LyricFontBold) FontWeight.Bold else null,
                                    textAlign = TextAlign.Left
                                )
                            }
                        ) {}
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures { /* 处理点击事件 */ }
                                    detectDragGestures { _, _ -> /* 处理拖动事件 */ }
                                }
                        ) {
                            // 空内容，用于拦截触摸事件
                        }
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSlider(
                        text = {
                            Text(text = stringResource(id = R.string.lyric_font_size))
                        },
                        subText = {
                            Text(text = "${SettingRepository.LyricFontSize}")
                        },
                        value = SettingRepository.LyricFontSize.toFloat(),
                        onValueChange = {
                            SettingRepository.LyricFontSize = it.toInt()
                        },
                        valueRange = 20f..40f
                    )
                    ItemDivider()
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.FormatBold,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.lyric_font_bold)) },
                        subText = { },
                        checked = SettingRepository.LyricFontBold,
                        onCheckedChange = {
                            SettingRepository.LyricFontBold = it
                        }
                    )
                    ItemDivider()
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Translate,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(text = stringResource(id = R.string.lyrics_translation))
                        },
                        subText = {},
                        checked = SettingRepository.EnableLyricsTranslation,
                        onCheckedChange = {
                            SettingRepository.EnableLyricsTranslation = it
                        }
                    )
                }
            }
        }
    }
}
