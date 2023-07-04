package com.zxhhyj.music.ui.sheet

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.zxhhyj.music.logic.repository.PlayListRepository
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.ui.sheet.item.HeadSongTitleItem

@Composable
fun AddToPlayListSheet(song: SongBean) {
    LazyColumn {
        item {
            HeadSongTitleItem(song = song)
        }
        items(PlayListRepository.playlist) {
            Text(text = it.name)
        }
    }
}