package studio.mandysa.music.ui.screen.single

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.item.SubTitleItem
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SingleScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    MediaPermission(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val localBeans = LocalMediaRepository.getSongs()
        val topAppBarState = rememberTopAppBarState()
        LazyColumn(
            modifier = Modifier.bindTopAppBarState(topAppBarState),
            contentPadding = padding
        ) {
            item {
                AppListButton(
                    onClick = { /*TODO*/ },
                    imageVector = Icons.Rounded.PlaylistPlay,
                    text = stringResource(id = studio.mandysa.music.R.string.play_list)
                )
            }
            item {
                AppListButton(
                    onClick = {
                        mainNavController.navigate(ScreenDestination.Album)
                    },
                    imageVector = Icons.Rounded.Album,
                    text = stringResource(id = studio.mandysa.music.R.string.album)
                )
            }
            item {
                AppListButton(
                    onClick = { /*TODO*/ },
                    imageVector = Icons.Rounded.Mic,
                    text = stringResource(id = studio.mandysa.music.R.string.singer)
                )
            }
            item {
                SubTitleItem(title = stringResource(id = R.string.single))
            }
            itemsIndexed(localBeans) { index, item ->
                SongItem(sheetNavController = sheetNavController, song = item) {
                    PlayManager.play(localBeans, index)
                }
            }
        }
        TopAppBar(
            state = topAppBarState,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = studio.mandysa.music.R.string.media_lib)
        ) {
            AppIcon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null,
                modifier = Modifier.clickable {
                    sheetNavController.navigate(BottomSheetDestination.BottomSheet {
                        LazyColumn {
                            item {
                                AppMenuButton(
                                    onClick = {
                                        sheetNavController.popAll()
                                        mainNavController.navigate(ScreenDestination.ScanMusic)
                                    },
                                    imageVector = Icons.Rounded.Source,
                                    text = stringResource(id = R.string.scan_music)
                                )
                            }
                            item {
                                AppMenuButton(
                                    onClick = {
                                        sheetNavController.popAll()
                                    },
                                    imageVector = Icons.Rounded.Settings,
                                    text = stringResource(id = R.string.setting)
                                )
                            }
                        }
                    })
                })
        }
    }
}
