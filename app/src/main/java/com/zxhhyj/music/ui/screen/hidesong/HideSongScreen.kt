package com.zxhhyj.music.ui.screen.hidesong

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.SoundQualityIcon
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item

@Composable
fun HiddenSongScreen(paddingValues: PaddingValues) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier,
                title = { Text(text = stringResource(id = R.string.hidden_songs)) }
            )
        }) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(AndroidMediaLibRepository.hideSongs) {
                    HideSongItem(song = it) {
                        AndroidMediaLibRepository.unHideSong(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun HideSongItem(
    song: SongBean,
    onClick: () -> Unit
) {
    Item(
        icon = {
            AppAsyncImage(modifier = Modifier.size(50.dp), data = song.coverUrl)
        },
        text = {
            Text(
                text = song.songName,
                color = LocalColorScheme.current.text,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        },
        subText = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (SettingRepository.EnableShowSoundQualityLabel) {
                    SoundQualityIcon(song = song)
                }
                Text(
                    text = song.artist.name,
                    color = LocalColorScheme.current.subText,
                    fontSize = 13.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.subText) {
                AppIconButton(onClick = {
                    onClick.invoke()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = null
                    )
                }
            }
        }) {}
}