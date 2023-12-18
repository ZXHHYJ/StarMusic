package com.zxhhyj.music.ui.screen.hidesong

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.roundColumn

@Composable
fun HiddenSongScreen(paddingValues: PaddingValues) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.hidden_songs)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            roundColumn {
                items(AndroidMediaLibRepository.hideSongs) {
                    SongItem(songBean = it, actions = {
                        CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.subText) {
                            AppIconButton(onClick = {
                                AndroidMediaLibRepository.unHideSong(it)
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Remove,
                                    contentDescription = null
                                )
                            }
                        }
                    }) {}
                }
            }
        }
    }
}
