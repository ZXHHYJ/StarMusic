package com.zxhhyj.music.ui.screen.play

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FontDownload
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mxalbert.sharedelements.FadeMode
import com.mxalbert.sharedelements.MaterialArcMotionFactory
import com.mxalbert.sharedelements.SharedElement
import com.mxalbert.sharedelements.SharedElementsRoot
import com.mxalbert.sharedelements.SharedElementsTransitionSpec
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.toTimeString
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.AlbumMotionBlur
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.common.KeepScreenOn
import com.zxhhyj.music.ui.common.PanelController
import com.zxhhyj.music.ui.common.PanelState
import com.zxhhyj.music.ui.common.lyric.Lyric
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.round
import com.zxhhyj.music.ui.theme.translucentWhiteColor
import com.zxhhyj.music.ui.theme.translucentWhiteFixBugColor
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.StarDimens
import com.zxhhyj.ui.view.AppCard
import com.zxhhyj.ui.view.AppDivider
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popUpTo
import dev.olshevski.navigation.reimagined.rememberNavController

private enum class PlayScreenDestination {
    Controller,
    Lyric,
    PlayQueue,
}

private val PlayScreenDestination.tabIcon
    get() = when (this) {
        PlayScreenDestination.Controller -> throw RuntimeException()
        PlayScreenDestination.Lyric -> Icons.Rounded.FontDownload
        PlayScreenDestination.PlayQueue -> Icons.Rounded.FormatListBulleted
    }

private const val ShareAlbumKey = "album"

private val AnimDurationMillis
    get() = if (!SettingRepository.EnableLinkUI) 300 else 0

private object PlayScreen {
    val PlayScreenContentHorizontal = StarDimens.horizontal
    val PlayScreenHorizontal = 12.dp
}

private val MaterialFadeInTransitionSpec
    get() = SharedElementsTransitionSpec(
        pathMotionFactory = MaterialArcMotionFactory,
        durationMillis = AnimDurationMillis,
        fadeMode = FadeMode.In
    )

private val MaterialFadeOutTransitionSpec
    get() = SharedElementsTransitionSpec(
        pathMotionFactory = MaterialArcMotionFactory,
        durationMillis = AnimDurationMillis,
        fadeMode = FadeMode.Out
    )

@Composable
fun PlayScreen(
    sheetNavController: NavController<SheetDestination>,
    panelController: PanelController
) {

    val navController = rememberNavController(startDestination = PlayScreenDestination.Controller)

    val lastDestination = navController.backstack.entries.last().destination

    if (panelController.panelState == PanelState.COLLAPSED) {
        navController.popUpTo {
            it == PlayScreenDestination.Controller
        }
    }

    SharedElementsRoot {
        BoxWithPercentages(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {

            @Composable
            fun BottomNavigationBar(modifier: Modifier) {
                BottomNavigation(
                    modifier = modifier.height(60.dp),
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                ) {
                    listOf(
                        PlayScreenDestination.Lyric,
                        PlayScreenDestination.PlayQueue
                    ).forEach { screen ->
                        val selected = screen == lastDestination
                        BottomNavigationItem(
                            modifier = Modifier.clip(RoundedCornerShape(round)),
                            icon = { Icon(screen.tabIcon, contentDescription = screen.name) },
                            selectedContentColor = Color.White,
                            unselectedContentColor = translucentWhiteColor,
                            selected = selected,
                            onClick = {
                                if (selected) {
                                    navController.popUpTo {
                                        it == PlayScreenDestination.Controller
                                    }
                                    return@BottomNavigationItem
                                }
                                if (!navController.moveToTop { it == screen }) {
                                    navController.navigate(screen)
                                }
                            }
                        )
                    }
                }
            }

            @Composable
            fun MainNavHost() {
                val visibilityTopMediaBar = maxWidth < maxHeight
                AnimatedNavHost(
                    controller = navController,
                    transitionSpec = { _, _, _ ->
                        if (SettingRepository.EnableLinkUI) {
                            return@AnimatedNavHost EnterTransition.None togetherWith ExitTransition.None
                        }
                        val tween = tween<Float>(durationMillis = AnimDurationMillis)
                        fadeIn(tween) togetherWith fadeOut(tween)
                    }
                ) {
                    when (it) {
                        PlayScreenDestination.Controller -> {
                            PlayControllerScreen(sheetNavController)
                        }

                        PlayScreenDestination.Lyric, PlayScreenDestination.PlayQueue -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                if (visibilityTopMediaBar) {
                                    LazyColumn(userScrollEnabled = false) {
                                        item {
                                            TopSongItem(
                                                navController = navController,
                                                sheetNavController = sheetNavController
                                            )
                                        }
                                    }
                                }
                                when (it) {
                                    PlayScreenDestination.Lyric -> PlayLyricScreen()
                                    PlayScreenDestination.PlayQueue -> PlayQueueScreen(
                                        panelController = panelController
                                    )

                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }

            when (SettingRepository.EnableLinkUI) {
                true -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray)
                    )
                }

                false -> {
                    val currentSong by PlayerManager.currentSongFlow.collectAsState()
                    AlbumMotionBlur(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray),
                        albumUrl = currentSong?.coverUrl,
                        paused = panelController.panelState == PanelState.COLLAPSED
                    )
                }
            }

            BackHandler(panelController.panelState == PanelState.EXPANDED) {
                panelController.swipeTo(PanelState.COLLAPSED)
            }
            when {
                maxWidth >= maxHeight -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                    ) {
                        Spacer(modifier = Modifier.width(12.wp))
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1.0f)
                                .padding(horizontal = PlayScreen.PlayScreenHorizontal),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1.0f)
                            ) {
                                PlayControllerScreen(sheetNavController)
                            }
                            BottomNavigationBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.wp))
                        if (lastDestination == PlayScreenDestination.Controller) {
                            navController.navigate(PlayScreenDestination.Lyric)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1.0f)
                                .padding(horizontal = PlayScreen.PlayScreenHorizontal)
                        ) {
                            MainNavHost()
                        }
                        Spacer(modifier = Modifier.width(12.wp))
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .padding(horizontal = PlayScreen.PlayScreenHorizontal),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1.0f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            BackHandler(lastDestination != PlayScreenDestination.Controller) {
                                navController.popUpTo { it == PlayScreenDestination.Controller }
                            }
                            MainNavHost()
                        }
                        BottomNavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayControllerScreen(sheetNavController: NavController<SheetDestination>) {
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

            val currentSong by PlayerManager.currentSongFlow.collectAsState()

            SharedElement(
                key = ShareAlbumKey,
                screenKey = this,
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
                        data = currentSong?.coverUrl
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
                        text = currentSong?.songName ?: "",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = currentSong?.artistName ?: "",
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
                            currentSong?.let {
                                sheetNavController.navigate(SheetDestination.SongPanel(it))
                            }
                        }
                )
            }

            ItemSpacer()

            val progress by PlayerManager.progressFlow.collectAsState()

            val duration by PlayerManager.durationFlow.collectAsState()

            val sliderHeight = 16.dp

            AppSlider(
                value = progress.toFloat(),
                valueRange = 0f..duration.toFloat(),
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
                    text = progress.toTimeString(),
                    color = translucentWhiteColor,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = duration.toTimeString(),
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
                CompositionLocalProvider(
                    LocalColorScheme provides LocalColorScheme.current.copy(disabled = translucentWhiteColor),
                    LocalContentColor provides Color.White
                ) {
                    val buttonSize = 64.dp
                    val index by PlayerManager.indexFlow.collectAsState()
                    val playlist by PlayerManager.playListFlow.collectAsState()

                    AppIconButton(
                        onClick = { PlayerManager.skipToPrevious() },
                        enabled = index != 0,
                        modifier = Modifier
                            .size(buttonSize)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(0.8f)
                        )
                    }

                    val pauseState by PlayerManager.pauseFlow.collectAsState()
                    val playPauseIcon =
                        if (pauseState) R.drawable.ic_play else R.drawable.ic_pause

                    AppIconButton(
                        onClick = {
                            if (pauseState) {
                                PlayerManager.start()
                            } else {
                                PlayerManager.pause()
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .size(buttonSize)
                            .scale(1.2f)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = playPauseIcon),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    AppIconButton(
                        onClick = { PlayerManager.skipToNext() },
                        enabled = index != playlist?.size?.let { it - 1 },
                        modifier = Modifier
                            .size(buttonSize)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(0.8f)
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun ColumnScope.PlayLyricScreen() {
    val pause by PlayerManager.pauseFlow.collectAsState()
    KeepScreenOn(enable = !pause)
    val currentSong by PlayerManager.currentSongFlow.collectAsState()
    val liveTime by PlayerManager.progressFlow.collectAsState()
    val animDurationMillis = 1000
    Lyric(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.0f),
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
                        horizontal = PlayScreen.PlayScreenContentHorizontal
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
    ) {
        PlayerManager.seekTo(it)
        PlayerManager.start()
    }
}

@Composable
private fun ColumnScope.PlayQueueScreen(panelController: PanelController) {
    val playlist by PlayerManager.playListFlow.collectAsState()
    val currentSong by PlayerManager.currentSongFlow.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PlayScreen.PlayScreenContentHorizontal,
                vertical = vertical
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.play_list),
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${(playlist?.indexOf(currentSong) ?: 0) + 1}/${playlist?.size}",
                color = translucentWhiteColor,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = stringResource(id = R.string.clear),
            color = translucentWhiteColor,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                panelController.swipeTo(PanelState.COLLAPSED)
                PlayerManager.clearPlayList()
            })
    }

    AppDivider(
        color = translucentWhiteColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PlayScreen.PlayScreenContentHorizontal)
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.0f)
    ) {

        val lazyListState = rememberLazyListState()

        var selectItemBoxSize by remember {
            mutableStateOf(IntSize.Zero)
        }

        val currentIndex by PlayerManager.indexFlow.collectAsState()

        CompositionLocalProvider(
            LocalColorScheme provides LocalColorScheme.current.copy(
                text = Color.White,
                subText = translucentWhiteColor
            ), LocalContentColor provides translucentWhiteColor
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(top = vertical)
            ) {
                playlist?.let { songBeans ->
                    itemsIndexed(songBeans) { index, model ->
                        AppCard(
                            backgroundColor = if (currentIndex == index) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                            modifier = Modifier
                                .onSizeChanged {
                                    selectItemBoxSize = it
                                }
                                .fillMaxWidth()
                        ) {
                            SongItem(
                                songBean = model,
                                actions = {
                                    AppIconButton(
                                        onClick = {
                                            PlayerManager.removeSong(index)
                                        }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Remove,
                                            contentDescription = null,
                                        )
                                    }
                                }) { PlayerManager.play(songBeans, index) }
                        }
                    }
                }
            }
        }
        val boxHeightPx = with(LocalDensity.current) {
            maxHeight.roundToPx()
        }
        LaunchedEffect(Unit) {
            val height = (boxHeightPx - selectItemBoxSize.height) / 2
            lazyListState.scrollToItem(currentIndex?.coerceAtLeast(0) ?: 0, -height)
        }
        LaunchedEffect(currentIndex) {
            val height = (boxHeightPx - selectItemBoxSize.height) / 2
            lazyListState.animateScrollToItem(currentIndex?.coerceAtLeast(0) ?: 0, -height)
        }
    }
}

@Composable
private fun TopSongItem(
    modifier: Modifier = Modifier,
    navController: NavController<PlayScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
) {
    val currentSong by PlayerManager.currentSongFlow.collectAsState()
    Row(
        modifier = modifier.padding(horizontal = PlayScreen.PlayScreenContentHorizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val albumHeight = 56.dp
        SharedElement(
            key = ShareAlbumKey,
            screenKey = this,
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
                    .size(albumHeight),
                data = currentSong?.coverUrl
            ) {
                navController.moveToTop {
                    it == PlayScreenDestination.Controller
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1.0f)
                .height(albumHeight),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = currentSong?.songName ?: "",
                color = Color.White,
                fontSize = 16.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = currentSong?.artistName ?: "",
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
                .clip(RoundedCornerShape(50))
                .background(translucentWhiteFixBugColor)
                .clickable {
                    currentSong?.let {
                        sheetNavController.navigate(SheetDestination.SongPanel(it))
                    }
                }
        )
    }
}