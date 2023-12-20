package com.zxhhyj.music.ui.screen.medialibconfig

import android.Manifest
import android.os.Build
import android.os.Environment
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppSwitch
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaLibConfigScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues
) {
    var isExternalStorageManager by rememberSaveable {
        mutableStateOf(false)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        isExternalStorageManager = Environment.isExternalStorageManager()
    }

    /**
     * 提拉米苏上是READ_MEDIA_AUDIO权限，否则是READ_EXTERNAL_STORAGE
     */
    val mediaAudioPermissionState =
        rememberPermissionState(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE)
    LaunchedEffect(SettingRepository.EnableAndroidMediaLibs, mediaAudioPermissionState.status) {
        if (SettingRepository.EnableAndroidMediaLibs) {
            when (mediaAudioPermissionState.status) {
                is PermissionStatus.Denied -> {
                    mediaAudioPermissionState.launchPermissionRequest()
                }

                PermissionStatus.Granted -> {}
            }
        }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.media_lib)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.LibraryMusic,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(text = stringResource(id = R.string.android_media_libs))
                        },
                        subText = {
                            Text(
                                text = stringResource(
                                    id = R.string.media_lib_has_songs_size,
                                    AndroidMediaLibRepository.songs.size
                                )
                            )
                        },
                        actions = {
                            AppSwitch(
                                checked = SettingRepository.EnableAndroidMediaLibs,
                                onCheckedChange = {
                                    SettingRepository.EnableAndroidMediaLibs = it
                                    PlayerManager.clearPlayList()
                                    if (!it) {
                                        AndroidMediaLibRepository.clear()
                                    }
                                })
                        },
                        enabled = SettingRepository.EnableAndroidMediaLibs
                    ) {
                        mainNavController.navigate(ScreenDestination.AndroidMediaLibSetting)
                    }
                    ItemDivider()
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.CloudUpload,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.webdav)) },
                        subText = {
                            Text(
                                text = stringResource(
                                    id = R.string.media_lib_has_songs_size,
                                    WebDavMediaLibRepository.songs.size
                                )
                            )
                        },
                        actions = {
                            AppSwitch(
                                checked = SettingRepository.EnableWebDav,
                                onCheckedChange = {
                                    SettingRepository.EnableWebDav = it
                                    PlayerManager.clearPlayList()
                                })
                        },
                        enabled = SettingRepository.EnableWebDav,
                    ) onClick@{
                        if (!SettingRepository.EnableStarMusicPro) {
                            mainNavController.navigate(ScreenDestination.Pro)
                            return@onClick
                        }
                        mainNavController.navigate(ScreenDestination.WebDavConfig)
                    }
                    ItemDivider()
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.refresh_all_media_lib)) },
                        subText = { },
                        enabled = SettingRepository.EnableAndroidMediaLibs || SettingRepository.EnableWebDav
                    ) {
                        if (SettingRepository.EnableAndroidMediaLibs) {
                            dialogNavController.navigate(DialogDestination.RefreshAndroidMediaLib)
                        }
                        if (SettingRepository.EnableWebDav) {
                            dialogNavController.navigate(DialogDestination.RefreshWebDavMediaLib)
                        }
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.AudioFile,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.cache_manager)) },
                        subText = { }
                    ) {
                        mainNavController.navigate(ScreenDestination.CacheManager)
                    }
                }
            }
        }
    }
}