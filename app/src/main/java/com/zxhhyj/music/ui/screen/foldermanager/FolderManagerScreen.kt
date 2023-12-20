package com.zxhhyj.music.ui.screen.foldermanager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppSwitch
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FolderManagerScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>
) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.folder_manager)) })
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val lifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
        val folders = remember {
            (AndroidMediaLibRepository.folders + AndroidMediaLibRepository.hideFolders).sortedBy { it.path }
        }
        LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = paddingValues) {
            roundColumn {
                items(folders) { folder ->
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Folder,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = folder.path.substringAfterLast("/")) },
                        subText = {
                            Text(
                                text = stringResource(
                                    id = R.string.total_n_songs,
                                    folder.songs.size
                                )
                            )
                        },
                        actions = {
                            AppSwitch(checked = AndroidMediaLibRepository.folders.any { it == folder },
                                onCheckedChange = {
                                    lifecycleCoroutineScope.launch(Dispatchers.IO) {
                                        if (it) {
                                            AndroidMediaLibRepository.unHideFolder(folder)
                                        } else {
                                            AndroidMediaLibRepository.hideFolder(folder)
                                        }
                                    }
                                })
                        }
                    ) {
                        mainNavController.navigate(ScreenDestination.SongsPreview(folder.songs))
                    }
                }
            }

        }
    }
}