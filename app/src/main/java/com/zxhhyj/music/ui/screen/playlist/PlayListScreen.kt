package com.zxhhyj.music.ui.screen.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.ui.common.stateprompt.StatePrompt
import com.zxhhyj.music.ui.item.PlayListItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun PlayListScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    dialogNavController: NavController<DialogDestination>,
    padding: PaddingValues
) {
    com.zxhhyj.ui.Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            com.zxhhyj.ui.TopBar(
                modifier = Modifier,
                title = stringResource(id = R.string.play_list),
                actions = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            dialogNavController.navigate(DialogDestination.CreatePlayList)
                        })
                })
        }) {
        StatePrompt(
            empty = PlayListRepository.playlist.isEmpty(),
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(PlayListRepository.playlist) {
                    PlayListItem(
                        model = it,
                        actions = {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        sheetNavController.navigate(
                                            BottomSheetDestination.PlaylistMenu(
                                                it
                                            )
                                        )
                                    }
                            )
                        },
                        onClick = {
                            mainNavController.navigate(ScreenDestination.PlayListCnt(it))
                        })
                }
            }
        }
    }
}