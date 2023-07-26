package com.zxhhyj.music.ui.screen.medialibs

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HideSource
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.common.AppScaffold
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.item.SettingItem
import com.zxhhyj.music.ui.item.SettingSwitchItem
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaSourceScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    padding: PaddingValues
) {
    val permissionState =
        rememberPermissionState(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE)
    LaunchedEffect(SettingRepository.EnableAndroidMediaLibs, permissionState.status) {
        if (SettingRepository.EnableAndroidMediaLibs) {
            when (permissionState.status) {
                is PermissionStatus.Denied -> {
                    permissionState.launchPermissionRequest()
                }

                PermissionStatus.Granted -> {
                    if (AndroidMediaLibsRepository.songs.isEmpty()) {
                        dialogNavController.navigate(DialogDestination.ScanMusic)
                    }
                }
            }
        } else {
            AndroidMediaLibsRepository.clear()
        }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.media_lib)
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                SettingSwitchItem(
                    imageVector = Icons.Rounded.LibraryMusic,
                    title = stringResource(id = R.string.android_meida_libs),
                    subTitle = stringResource(
                        id = R.string.media_lib_has_songs_size,
                        AndroidMediaLibsRepository.songs.size
                    ),
                    checked = SettingRepository.EnableAndroidMediaLibs,
                    onCheckedChange = {
                        SettingRepository.EnableAndroidMediaLibs = it
                    }
                )
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.Refresh,
                    title = stringResource(id = R.string.refresh_media_lib),
                    enabled = SettingRepository.EnableAndroidMediaLibs
                ) {
                    dialogNavController.navigate(DialogDestination.ScanMusic)
                }
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.HideSource,
                    title = stringResource(id = R.string.hidden_songs),
                    subTitle = stringResource(id = R.string.hidden_songs),
                    enabled = SettingRepository.EnableAndroidMediaLibs
                ) {
                    mainNavController.navigate(ScreenDestination.HiddenSong)
                }
            }
        }
    }
}