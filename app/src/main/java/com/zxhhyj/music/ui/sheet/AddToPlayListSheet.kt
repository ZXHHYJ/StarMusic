package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.common.AppButton
import com.zxhhyj.music.ui.common.stateprompt.EmptyContentMessage
import com.zxhhyj.music.ui.item.PlayListItem
import com.zxhhyj.music.ui.screen.BottomSheetDestination
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.theme.horizontal
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll

@Composable
fun AddToPlayListSheet(
    dialogNavController: NavController<DialogDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    song: SongBean
) {
    AppButton(
        onClick = { dialogNavController.navigate(DialogDestination.CreatePlayList) },
        imageVector = Icons.Rounded.Add,
        text = stringResource(id = R.string.create_playlist),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal)
    )
    if (PlayListRepository.playlist.isEmpty()) {
        EmptyContentMessage(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    } else {
        LazyColumn {
            items(PlayListRepository.playlist) {
                PlayListItem(it) {
                    it.addSong(song)
                    sheetNavController.popAll()
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}