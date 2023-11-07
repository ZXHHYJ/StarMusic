package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.ui.view.YesNoDialog
import java.io.File

@Composable
fun DeleteSongDialog(
    onDismissRequest: () -> Unit,
    songBean: SongBean
) {
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.delete),
        confirm = {
            Text(
                text = stringResource(id = R.string.permanent_delete),
                modifier = Modifier.clickable {
                    when (songBean) {
                        is SongBean.Local -> {
                            val file = File(songBean.data)
                            file.delete()
                            AndroidMediaLibRepository.removeSong(songBean)
                        }

                        is SongBean.WebDav -> {
                            val file = File(songBean.data)
                            file.delete()
                            WebDavMediaLibRepository.removeSong(songBean)
                        }

                        else -> {}
                    }
                    onDismissRequest.invoke()
                })
            if (songBean is SongBean.Local) {
                Text(
                    text = stringResource(id = R.string.delete_from_media_library),
                    modifier = Modifier.clickable {
                        AndroidMediaLibRepository.removeSong(songBean)
                        onDismissRequest.invoke()
                    })
            }
        },
        dismiss = {})
    {
        Text(text = "\"${songBean.songName}\"")
    }
}