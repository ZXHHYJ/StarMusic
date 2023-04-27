package com.zxhhyj.music.ui.screen.mediasource

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
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
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.media.MediaLibsRepository
import com.zxhhyj.music.ui.common.topbar.TopAppBar
import com.zxhhyj.music.ui.common.topbar.bindTopAppBarState
import com.zxhhyj.music.ui.common.topbar.rememberTopAppBarState
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.setting.item.SettingItem
import com.zxhhyj.music.ui.screen.setting.item.SettingSwitchItem
import dev.olshevski.navigation.reimagined.NavController

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun MediaSourceScreen(
    mainNavController: NavController<ScreenDestination>,
    padding: PaddingValues
) {
    val topAppBarState = rememberTopAppBarState()
    val mediaLibsManager = MediaLibsRepository.rememberMediaLibsManager()
    val permissionState =
        rememberPermissionState(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE)
    LaunchedEffect(SettingRepository.EnableAndroidMediaLibs, permissionState.status) {
        if (SettingRepository.EnableAndroidMediaLibs) {
            if (permissionState.status is PermissionStatus.Denied) {
                permissionState.launchPermissionRequest()
            }
        } else {
            mediaLibsManager.clear()
        }
    }
    LazyColumn(
        modifier = Modifier
            .bindTopAppBarState(topAppBarState)
            .fillMaxSize()
            .padding(padding)
    ) {
        item {
            SettingSwitchItem(
                imageVector = Icons.Rounded.LibraryMusic,
                title = stringResource(id = R.string.android_meida_libs),
                subTitle = "当前媒体库中包含" + MediaLibsRepository.songs.size + "首歌曲",
                checked = SettingRepository.EnableAndroidMediaLibs,
                onCheckedChange = {
                    SettingRepository.EnableAndroidMediaLibs = it
                }
            )
        }
        item {
            AnimatedVisibility(visible = SettingRepository.EnableAndroidMediaLibs) {
                SettingItem(
                    modifier = Modifier.animateItemPlacement(),
                    imageVector = Icons.Rounded.Refresh,
                    title = "刷新媒体库"
                ) {
                    mediaLibsManager.scanMedia()
                }
            }
        }
    }
    TopAppBar(
        state = topAppBarState,
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.media_lib)
    )
}