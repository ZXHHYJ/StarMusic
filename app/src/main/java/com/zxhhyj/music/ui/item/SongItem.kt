package com.zxhhyj.music.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.Downloading
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavFile
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
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
fun SongItem(
    sheetNavController: NavController<SheetDestination>,
    songBean: SongBean,
    actions: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
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
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (SettingRepository.EnableShowSoundQualityLabel) {
                    SoundQualityIcon(song = songBean)
                }
                if (songBean is SongBean.WebDav) {
                    WebDavIcon()
                }
                Text(
                    text = songBean.artist.name,
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
            }
        }) {
        onClick.invoke()
    }
}

@Composable
fun SongItem(
    sheetNavController: NavController<SheetDestination>,
    webDavFile: WebDavFile,
    onClick: () -> Unit,
    downloadedOnClick: (SongBean.WebDav) -> Unit
) {
    val webDavSongBean = WebDavMediaLibRepository.songs.find { it.webDavFile == webDavFile }
    if (webDavSongBean != null) {
        SongItem(sheetNavController = sheetNavController, songBean = webDavSongBean) {
            downloadedOnClick.invoke(webDavSongBean)
        }
    } else {
        Item(
            icon = {
                Box(contentAlignment = Alignment.Center) {
                    AppAsyncImage(
                        modifier = Modifier
                            .size(50.dp),
                        backgroundColor = LocalColorScheme.current.highlight,
                        data = null
                    )
                    CompositionLocalProvider(LocalContentColor provides Color.White) {
                        if (WebDavMediaLibRepository.downloadList.contains(webDavFile)) {
                            Icon(
                                imageVector = Icons.Rounded.Downloading,
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.AudioFile,
                                contentDescription = null
                            )
                        }
                    }
                }
            },
            text = {
                Text(
                    text = webDavFile.name,
                    color = LocalColorScheme.current.text,
                    fontSize = 15.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            },
            subText = {
                Text(
                    text = webDavFile.path,
                    color = LocalColorScheme.current.subText,
                    fontSize = 13.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            },
            actions = {
                CompositionLocalProvider(LocalContentColor provides LocalColorScheme.current.subText) {
                    AppIconButton(
                        onClick = {

                        }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            }) {
            onClick.invoke()
        }
    }
}

/**
 * 展示用，没有点击事件
 */
@Composable
fun SongItem(song: SongBean) {
    Item(
        icon = {
            AppAsyncImage(
                modifier = Modifier
                    .size(50.dp),
                data = song.coverUrl
            )
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
        },
        onClick = {}
    )
}