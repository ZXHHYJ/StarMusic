package com.zxhhyj.music.ui.screen.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
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
import com.zxhhyj.ui.view.IconButton
import com.zxhhyj.ui.view.ListButton
import com.zxhhyj.ui.view.Scaffold
import com.zxhhyj.ui.view.TopBar
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
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.media_lib),
                actions = {
                    IconButton(onClick = {
                        mainNavController.navigate(
                            ScreenDestination.Search(
                                SearchScreenTabs.Single
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    }
                    IconButton(onClick = {
                        mainNavController.navigate(
                            ScreenDestination.Setting
                        )
                    }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                    }
                }
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ListButton(
                    onClick = {
                        mainNavController.navigate(ScreenDestination.PlayList)
                    },
                    imageVector = Icons.Rounded.PlaylistPlay,
                    text = stringResource(id = R.string.play_list)
                )
            }
            item {
                ListButton(
                    onClick = {
                        mainNavController.navigate(ScreenDestination.Album)
                    },
                    imageVector = Icons.Rounded.Album,
                    text = stringResource(id = R.string.album)
                )
            }
            item {
                ListButton(
                    onClick = {
                        mainNavController.navigate(ScreenDestination.Singer)
                    },
                    imageVector = Icons.Rounded.Mic,
                    text = stringResource(id = R.string.singer)
                )
            }
            item {
                SubTitleItem(title = stringResource(id = R.string.single))
            }
            itemsIndexed(AndroidMediaLibsRepository.songs) { index, item ->
                SongItem(sheetNavController = sheetNavController, song = item) {
                    PlayManager.play(AndroidMediaLibsRepository.songs, index)
                }
            }
        }
    }
}
