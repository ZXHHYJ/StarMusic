package com.zxhhyj.music.ui.screen.medialibconfig

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.HideSource
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
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.VipIcon
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaLibConfigScreen(
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
                    if (AndroidMediaLibRepository.folders.isEmpty() && AndroidMediaLibRepository.hideFolders.isEmpty()) {
                        dialogNavController.navigate(DialogDestination.ScanAndroidMediaLib)
                    }
                }
            }
        }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.media_lib)) })
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
                        checked = SettingRepository.EnableAndroidMediaLibs,
                        onCheckedChange = {
                            SettingRepository.EnableAndroidMediaLibs = it
                            if (!it) {
                                PlayerManager.clearPlayList()
                                AndroidMediaLibRepository.clear()
                            }
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
                        dialogNavController.navigate(DialogDestination.ScanAndroidMediaLib)
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
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Folder,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.folder_manager)) },
                        subText = { },
                        enabled = SettingRepository.EnableAndroidMediaLibs
                    ) {
                        mainNavController.navigate(ScreenDestination.FolderManager)
                    }
                    ItemDivider()
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.HideSource,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.hidden_songs)) },
                        subText = { },
                        enabled = SettingRepository.EnableAndroidMediaLibs
                    ) {
                        mainNavController.navigate(ScreenDestination.HiddenSong)
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    var isExternalStorageManager by rememberSaveable {
                        mutableStateOf(false)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        isExternalStorageManager = Environment.isExternalStorageManager()
                    }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult(),
                        onResult = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                isExternalStorageManager =
                                    Environment.isExternalStorageManager()
                                if (!isExternalStorageManager) {
                                    SettingRepository.EnableCueSupport = false
                                }
                            }
                        }
                    )
                    LaunchedEffect(SettingRepository.EnableCueSupport) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SettingRepository.EnableCueSupport && !isExternalStorageManager) {
                            launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                        }
                    }
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.AudioFile,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(text = stringResource(id = R.string.cue_support))
                        },
                        subText = {
                            VipIcon()
                        },
                        checked = SettingRepository.EnableCueSupport,
                        onCheckedChange = onCheckedChange@{
                            if (it && !SettingRepository.EnableStarMusicPro) {
                                mainNavController.navigate(ScreenDestination.Pro)
                                return@onCheckedChange
                            }
                            SettingRepository.EnableCueSupport = it
                        }
                    )
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
                                imageVector = Icons.Rounded.CloudUpload,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.webdav)) },
                        subText = {
                            VipIcon()
                        },
                    ) onClick@{
                        if (!SettingRepository.EnableStarMusicPro) {
                            mainNavController.navigate(ScreenDestination.Pro)
                            return@onClick
                        }
                        mainNavController.navigate(ScreenDestination.WebDavConfig)
                    }
                }
            }
        }
    }
}