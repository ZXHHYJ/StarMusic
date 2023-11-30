package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.logic.repository.PlayListRepository.addSong
import com.zxhhyj.music.ui.item.PlayListItem
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll

@Composable
fun AddToPlayListSheet(
    dialogNavController: NavController<DialogDestination>,
    sheetNavController: NavController<SheetDestination>,
    songBean: SongBean
) {
    RoundColumn(modifier = Modifier.fillMaxWidth()) {
        Item(
            icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) },
            text = { Text(text = stringResource(id = R.string.create_playlist)) },
            subText = { }) {
            dialogNavController.navigate(DialogDestination.CreatePlayList)
        }
    }
    ItemSpacer()
    RoundColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
    ) {
        LazyColumn {
            items(PlayListRepository.playList) {
                PlayListItem(it) {
                    it.addSong(songBean)
                    sheetNavController.popAll()
                }
            }
        }
    }

}