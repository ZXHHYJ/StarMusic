package com.zxhhyj.music.ui.dialog

import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.YesNoDialog
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popAll
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun DeleteSongDialog(
    dialogNavController: NavController<DialogDestination>,
    sheetNavController: NavController<SheetDestination>,
    songBean: SongBean,
) {
    val deleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if (it.resultCode == -1) {
                AndroidMediaLibRepository.removeSong(songBean as SongBean.Local)
                dialogNavController.pop()
                sheetNavController.popAll()
            }
        })
    val lifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
    YesNoDialog(
        onDismissRequest = {
            dialogNavController.pop()
        },
        title = stringResource(id = R.string.delete),
        positive = {
            when (songBean) {
                is SongBean.Local -> {
                    Text(text = stringResource(id = R.string.permanent_delete),
                        modifier = Modifier.clickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && songBean.id != null) {
                                lifecycleCoroutineScope.launch {
                                    val deleteUri = ContentUris.withAppendedId(
                                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songBean.id
                                    )
                                    val pendingIntent = MediaStore.createDeleteRequest(
                                        MainApplication.context.contentResolver,
                                        listOf(deleteUri)
                                    )
                                    val request =
                                        IntentSenderRequest.Builder(pendingIntent.intentSender)
                                            .build()
                                    deleteLauncher.launch(request)
                                }
                            } else {
                                val file = File(songBean.data)
                                if (file.delete()) {
                                    AndroidMediaLibRepository.removeSong(songBean)
                                    sheetNavController.popAll()
                                }
                            }
                        })
                }

                is SongBean.WebDav -> {
                    Text(text = stringResource(id = R.string.delete_locally),
                        modifier = Modifier.clickable {
                            PlayerManager.MediaCacheManager.getCacheFile(songBean.data)
                                ?.takeIf { it.delete() }?.let {
                                    WebDavMediaLibRepository.removeSong(songBean)
                                    dialogNavController.pop()
                                    sheetNavController.popAll()
                                }
                        })
                }
            }

            if (songBean is SongBean.Local) {
                Text(text = stringResource(id = R.string.delete_from_media_library),
                    modifier = Modifier.clickable {
                        AndroidMediaLibRepository.removeSong(songBean)
                        dialogNavController.pop()
                        sheetNavController.popAll()
                    })
            }
        },
        negative = {}) {
        Text(text = "\"${songBean.songName}\"")
    }
}