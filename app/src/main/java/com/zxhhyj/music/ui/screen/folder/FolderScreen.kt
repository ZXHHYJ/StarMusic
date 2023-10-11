package com.zxhhyj.music.ui.screen.folder

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun FolderScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>
) {
    AppScaffold(
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.folder)
            ) {

            }
        }, modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues)
    ) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                val folders = (AndroidMediaLibsRepository.folders).sortedBy { it.path }
                items(folders) { folder ->
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Folder,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = folder.path) },
                        subText = {
                            Text(
                                text = stringResource(
                                    id = R.string.total_n_songs,
                                    folder.songs.size
                                )
                            )
                        },
                        actions = {}
                    ) {
                        mainNavController.navigate(ScreenDestination.FolderCnt(folder))
                    }
                }
            }
        }
    }
}