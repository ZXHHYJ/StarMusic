package com.zxhhyj.music.ui.screen.medialibs

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Folder
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
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaSourceScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues
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
                    if (AndroidMediaLibsRepository.folders.isEmpty() && AndroidMediaLibsRepository.hideFolders.isEmpty()) {
                        dialogNavController.navigate(DialogDestination.ScanMusic)
                    }
                }
            }
        } else {
            PlayManager.clearPlayList()
            AndroidMediaLibsRepository.clear()
        }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.media_lib)
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.LibraryMusic,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(text = stringResource(id = R.string.android_meida_libs))
                        },
                        subText = {
                            Text(
                                text = stringResource(
                                    id = R.string.media_lib_has_songs_size,
                                    AndroidMediaLibsRepository.songs.size
                                )
                            )
                        },
                        checked = SettingRepository.EnableAndroidMediaLibs,
                        onCheckedChange = {
                            SettingRepository.EnableAndroidMediaLibs = it
                        }
                    )
                    ItemDivider()
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.refresh_media_lib)) },
                        subText = { },
                        enabled = SettingRepository.EnableAndroidMediaLibs
                    ) {
                        PlayManager.clearPlayList()
                        dialogNavController.navigate(DialogDestination.ScanMusic)
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.FilterAlt,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.exclude_songs_under_one_minute)) },
                        subText = { },
                        checked = SettingRepository.EnableExcludeSongsUnderOneMinute,
                        onCheckedChange = {
                            SettingRepository.EnableExcludeSongsUnderOneMinute = it
                        }
                    )
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Folder,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.folder_manager)) },
                        subText = { Text(text = stringResource(id = R.string.folder_manager)) },
                        enabled = SettingRepository.EnableAndroidMediaLibs
                    ) {
                        mainNavController.navigate(ScreenDestination.FolderManager)
                    }
                    ItemDivider()
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.HideSource,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.hidden_songs)) },
                        subText = { Text(text = stringResource(id = R.string.hidden_songs)) },
                        enabled = SettingRepository.EnableAndroidMediaLibs
                    ) {
                        mainNavController.navigate(ScreenDestination.HiddenSong)
                    }
                }
            }
        }
    }
}