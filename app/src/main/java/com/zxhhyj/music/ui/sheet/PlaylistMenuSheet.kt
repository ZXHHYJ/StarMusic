package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popUpTo

@Composable
fun PlaylistMenuSheet(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    model: PlayListModel
) {
    LazyColumn {
        item {
            com.zxhhyj.ui.ListButton(
                onClick = {
                    sheetNavController.pop()
                    dialogNavController.navigate(DialogDestination.EditPlayListTitle(model))
                },
                imageVector = Icons.Rounded.Edit,
                text = stringResource(id = R.string.edit_playlist_title)
            )
        }
        item {
            com.zxhhyj.ui.ListButton(
                onClick = {
                    sheetNavController.pop()
                    mainNavController.popUpTo {
                        it == ScreenDestination.PlayList
                    }
                    PlayListRepository.remove(model)
                },
                imageVector = Icons.Rounded.Delete,
                text = stringResource(id = R.string.delete)
            )
        }
    }
}