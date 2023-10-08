package com.zxhhyj.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.coverUrl
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.SoundQualityIcon
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppIconButton
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SongItem(
    sheetNavController: NavController<SheetDestination>,
    song: SongBean,
    actions: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAsyncImage(
            modifier = Modifier
                .padding(horizontal = horizontal, vertical = vertical)
                .size(50.dp),
            data = song.album.coverUrl
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = vertical),
        ) {
            Text(
                text = song.songName,
                color = LocalColorScheme.current.text,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Row(verticalAlignment = Alignment.CenterVertically) {
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
        }
        Row(horizontalArrangement = Arrangement.spacedBy(horizontal / 2)) {
            CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.subText) {
                actions.invoke()
                AppIconButton(onClick = {
                    sheetNavController.navigate(
                        SheetDestination.SongMenu(
                            song
                        )
                    )
                }) {
                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                }
            }
        }
        Spacer(modifier = Modifier.padding(end = horizontal))
    }
}

/**
 * 展示用，没有点击事件
 */
@Composable
fun SongItem(
    song: SongBean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAsyncImage(
            modifier = Modifier
                .padding(horizontal = horizontal, vertical = vertical)
                .size(50.dp),
            data = song.album.coverUrl
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = vertical),
        ) {
            Text(
                text = song.songName,
                color = LocalColorScheme.current.text,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Row(verticalAlignment = Alignment.CenterVertically) {
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
        }
        Spacer(modifier = Modifier.padding(end = horizontal))
    }
}