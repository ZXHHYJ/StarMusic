package com.zxhhyj.music.ui.screen.single

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.item.SubTitleItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTopBar
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.toolbarHeight
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SingleScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues
) {
    DisposableEffect(SettingRepository.AgreePrivacyPolicy, AndroidMediaLibsRepository.songs) {
        if (SettingRepository.AgreePrivacyPolicy && AndroidMediaLibsRepository.songs.isEmpty()) {
            dialogNavController.navigate(DialogDestination.MediaLibsEmpty)
        }
        onDispose { }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColorScheme.current.subBackground)
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.media_lib),
                content = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.media_lib),
                            color = LocalColorScheme.current.text,
                            fontSize = 26.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontal)
                                .height(toolbarHeight)
                        )
                        RoundColumn(modifier = Modifier.fillMaxWidth()) {
                            Item(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.PlaylistPlay,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.play_list)) },
                                subText = { }) {
                                mainNavController.navigate(ScreenDestination.PlayList)
                            }
                            ItemDivider()
                            Item(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Album,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.album)) },
                                subText = { }) {
                                mainNavController.navigate(ScreenDestination.Album)
                            }
                            ItemDivider()
                            Item(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Mic,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.singer)) },
                                subText = { }) {
                                mainNavController.navigate(ScreenDestination.Singer)
                            }

                        }
                    }
                },
                actions = {
                    AppIconButton(onClick = {
                        mainNavController.navigate(
                            ScreenDestination.Search(
                                SearchScreenTabs.Single
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    }
                    AppIconButton(onClick = {
                        mainNavController.navigate(
                            ScreenDestination.Setting
                        )
                    }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                    }
                }
            )
        }) {
        Column(modifier = Modifier.fillMaxSize()) {
            SubTitleItem(title = stringResource(id = R.string.single))
            RoundColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(AndroidMediaLibsRepository.songs) { index, item ->
                        SongItem(sheetNavController = sheetNavController, song = item) {
                            PlayManager.play(AndroidMediaLibsRepository.songs, index)
                        }
                    }
                }
            }
        }

    }
}
