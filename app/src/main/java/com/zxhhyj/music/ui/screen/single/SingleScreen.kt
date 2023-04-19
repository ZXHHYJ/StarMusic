package com.zxhhyj.music.ui.screen.single

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.LocalMediaRepository
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.common.AppIcon
import com.zxhhyj.music.ui.common.AppListButton
import com.zxhhyj.music.ui.common.MediaPermission
import com.zxhhyj.music.ui.common.TopAppBar
import com.zxhhyj.music.ui.common.bindTopAppBarState
import com.zxhhyj.music.ui.common.rememberTopAppBarState
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.item.SubTitleItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun SingleScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    dialogNavController: NavController<DialogDestination>,
    padding: PaddingValues
) {
    LaunchedEffect(LocalMediaRepository.songs) {
        if (LocalMediaRepository.songs.isEmpty()) {
            dialogNavController.navigate(DialogDestination.ScanMusic)
        }
    }
    MediaPermission(modifier = Modifier.fillMaxSize()) {
        val topAppBarState = rememberTopAppBarState()
        LazyColumn(
            modifier = Modifier.bindTopAppBarState(topAppBarState),
            contentPadding = padding
        ) {
            item {
                AppListButton(
                    onClick = {
                        mainNavController.navigate(ScreenDestination.PlayList)
                    },
                    imageVector = Icons.Rounded.PlaylistPlay,
                    text = stringResource(id = R.string.play_list)
                )
            }
            item {
                AppListButton(
                    onClick = {
                        mainNavController.navigate(ScreenDestination.Album)
                    },
                    imageVector = Icons.Rounded.Album,
                    text = stringResource(id = R.string.album)
                )
            }
            item {
                AppListButton(
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
            itemsIndexed(LocalMediaRepository.songs) { index, item ->
                SongItem(sheetNavController = sheetNavController, song = item) {
                    PlayManager.play(LocalMediaRepository.songs, index)
                }
            }
        }
        TopAppBar(
            state = topAppBarState,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.media_lib)
        ) {
            AppIcon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null,
                modifier = Modifier.clickable {
                    mainNavController.navigate(ScreenDestination.Setting)
                }
            )
        }
    }
}
