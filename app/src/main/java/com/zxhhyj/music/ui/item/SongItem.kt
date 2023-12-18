package com.zxhhyj.music.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.music.ui.common.SoundQualityIcon
import com.zxhhyj.music.ui.common.WebDavIcon
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.item.Item
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SongItem(songBean: SongBean, actions: @Composable () -> Unit, onClick: () -> Unit) {
    Item(
        icon = {
            AppAsyncImage(
                modifier = Modifier
                    .size(50.dp),
                data = songBean.coverUrl
            )
        },
        text = {
            Text(
                text = songBean.songName,
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
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                if (SettingRepository.EnableShowSoundQualityLabel && songBean.bitrate != null) {
                    SoundQualityIcon(song = songBean)
                }
                if (songBean is SongBean.WebDav) {
                    WebDavIcon()
                }
                Text(
                    text = songBean.artistName?: stringResource(id = R.string.unknown_artist),
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
                actions.invoke()
            }
        }) {
        onClick.invoke()
    }
}

@Composable
fun SongItem(
    sheetNavController: NavController<SheetDestination>,
    songBean: SongBean,
    actions: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    SongItem(
        songBean = songBean,
        actions = {
            actions.invoke()
            AppIconButton(onClick = {
                sheetNavController.navigate(
                    SheetDestination.SongMenu(
                        songBean
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )
            }
        },
        onClick = onClick
    )
}

@Composable
fun SongItem(songBean: SongBean) {
    SongItem(songBean = songBean, actions = {}) {}
}