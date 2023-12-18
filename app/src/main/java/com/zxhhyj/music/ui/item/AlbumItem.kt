package com.zxhhyj.music.ui.item

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.ui.common.AppAsyncImage
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.item.ItemArrowRight

@Composable
fun AlbumItem(
    albumName: String,
    onClick: () -> Unit
) {
    val songs = MediaLibHelper.Album[albumName]
    ItemArrowRight(
        icon = {
            AppAsyncImage(
                modifier = Modifier
                    .size(50.dp),
                data = songs.getOrNull(0)?.coverUrl
            )
        },
        text = {
            Text(
                text = albumName,
                color = LocalColorScheme.current.text,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        },
        subText = {
            Text(
                text = stringResource(id = R.string.total_n_songs, songs.size),
                color = LocalColorScheme.current.subText,
                fontSize = 13.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }) {
        onClick.invoke()
    }
}