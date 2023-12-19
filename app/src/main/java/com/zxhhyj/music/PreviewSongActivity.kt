package com.zxhhyj.music

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zxhhyj.mediaplayer.CueMediaPlayer
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.getRealPathFromUri
import com.zxhhyj.music.logic.utils.toSongBeanLocal
import com.zxhhyj.music.logic.utils.toTimeString
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AlbumMotionBlur
import com.zxhhyj.music.ui.common.lyric.Lyric
import com.zxhhyj.music.ui.theme.BottomSheetAnimation
import com.zxhhyj.music.ui.theme.StarMusicTheme
import com.zxhhyj.music.ui.theme.round
import com.zxhhyj.music.ui.theme.translucentWhiteColor
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.StarDimens
import com.zxhhyj.ui.view.AppCard
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.material.BottomSheetNavHost
import dev.olshevski.navigation.reimagined.material.BottomSheetProperties
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController
import java.io.File

class PreviewSongActivity : ComponentActivity() {

    private val cueMediaPlayer = CueMediaPlayer(MainApplication.context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        PlayerManager.pause()
        setContent {

            rememberSystemUiController().setSystemBarsColor(
                Color.Transparent,
                false,
                isNavigationBarContrastEnforced = false
            )

            StarMusicTheme {
                intent?.data?.let { uri ->
                    val navController = rememberNavController<String>(listOf())
                    NavBackHandler(controller = navController)
                    BottomSheetNavHost(
                        controller = navController,
                        onDismissRequest = {
                            finish()
                        },
                        sheetPropertiesSpec = {
                            BottomSheetProperties(
                                animationSpec = if (SettingRepository.EnableLinkUI) tween(0) else BottomSheetAnimation,
                                skipHalfExpanded = true
                            )
                        }) {
                        val pause by cueMediaPlayer.pauseFlow.collectAsState()
                        val songBean = getRealPathFromUri(
                            LocalContext.current, uri
                        )?.let { path -> File(path).toSongBeanLocal() }
                        songBean?.let {
                            cueMediaPlayer.preparePlay(songBean)
                        }
                        AppCard(
                            backgroundColor = LocalColorScheme.current.background,
                            shape = RoundedCornerShape(topStart = round, topEnd = round)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                            ) {
                                item {
                                    AppCenterTopBar(title = {
                                        Text(text = "${songBean?.songName}")
                                    })
                                }
                                item {
                                    val progress by cueMediaPlayer.currentProgressFlow.collectAsState()
                                    val duration by cueMediaPlayer.songDurationFlow.collectAsState()
                                    RoundColumn(modifier = Modifier.fillMaxWidth()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(max = 400.dp)
                                        ) {
                                            AlbumMotionBlur(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.DarkGray)
                                                    .clipToBounds(),
                                                albumUrl = songBean?.coverUrl,
                                                paused = false
                                            )
                                            //这里参数与播放界面的歌词要保持一致
                                            val animDurationMillis = 1000
                                            Column(modifier = Modifier.fillMaxSize()) {
                                                Lyric(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(1.0f),
                                                    lyric = songBean?.lyric,
                                                    liveTime = progress,
                                                    translation = SettingRepository.EnableLyricsTranslation,
                                                    lyricScrollAnimationSpec = tween(
                                                        animDurationMillis
                                                    ),
                                                    lyricItem = { modifier: Modifier,
                                                                  model: String,
                                                                  index: Int,
                                                                  position: Int ->
                                                        val textColor by animateColorAsState(
                                                            targetValue = if (position == index) Color.White else translucentWhiteColor,
                                                            label = "lyric_text_color",
                                                            animationSpec = tween(
                                                                animDurationMillis
                                                            )
                                                        )
                                                        val textScale by animateFloatAsState(
                                                            targetValue = if (position == index) 1.05f else 1f,
                                                            label = "lyric_text_scale",
                                                            animationSpec = tween(
                                                                animDurationMillis
                                                            )
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
                                                                    transformOrigin =
                                                                        TransformOrigin(0f, 0f)
                                                                },
                                                            text = model,
                                                            color = textColor,
                                                            fontSize = SettingRepository.LyricFontSize.sp,
                                                            fontWeight = if (SettingRepository.LyricFontBold) FontWeight.Bold else null,
                                                            textAlign = TextAlign.Left
                                                        )
                                                    }
                                                ) {
                                                    cueMediaPlayer.seekTo(it)
                                                }
                                                CompositionLocalProvider(
                                                    LocalColorScheme provides LocalColorScheme.current.copy(
                                                        highlight = Color.White,
                                                        subText = Color.White,
                                                        disabled = translucentWhiteColor
                                                    )
                                                ) {
                                                    ItemSlider(
                                                        text = { Text(text = progress.toTimeString()) },
                                                        subText = { Text(text = duration.toTimeString()) },
                                                        value = progress.toFloat(),
                                                        valueRange = 0f..duration.toFloat(),
                                                        onValueChange = {
                                                            cueMediaPlayer.seekTo(it.toInt())
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                item {
                                    ItemSpacer()
                                }
                                item {
                                    RoundColumn(modifier = Modifier.fillMaxWidth()) {
                                        Item(
                                            icon = {
                                                Icon(
                                                    imageVector = if (!pause) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                                    contentDescription = null
                                                )
                                            },
                                            text = {
                                                Text(
                                                    text = if (!pause) stringResource(id = R.string.pause) else stringResource(
                                                        id = R.string.play
                                                    )
                                                )
                                            },
                                            subText = {}) {
                                            if (cueMediaPlayer.pauseFlow.value) {
                                                cueMediaPlayer.start()
                                            } else {
                                                cueMediaPlayer.pause()
                                            }
                                        }
                                        ItemDivider()
                                        Item(
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.Rounded.Add,
                                                    contentDescription = null
                                                )
                                            },
                                            text = {
                                                Text(text = stringResource(id = R.string.add_to_media_libs))
                                            },
                                            subText = {},
                                            enabled = SettingRepository.EnableAndroidMediaLibs
                                        ) {
                                            cueMediaPlayer.pause()
                                            AndroidMediaLibRepository.addSong(songBean!!)
                                            startActivity(
                                                Intent(
                                                    this@PreviewSongActivity,
                                                    MainActivity::class.java
                                                )
                                            )
                                            PlayerManager.play(listOf(songBean), 0)
                                            finish()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (navController.backstack.entries.isEmpty()) {
                        navController.navigate(String())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cueMediaPlayer.release()
    }
}