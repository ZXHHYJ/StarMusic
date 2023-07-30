package com.zxhhyj.music.ui.screen.setting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.FontDownload
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Source
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.utils.VersionUtils
import com.zxhhyj.music.ui.common.AppScaffold
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.item.SettingItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SettingScreen(
    mainNavController: NavController<ScreenDestination>,
    padding: PaddingValues,
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            AppTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.setting)
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                SettingItem(
                    imageVector = Icons.Rounded.ColorLens,
                    title = stringResource(id = R.string.theme),
                    subTitle = stringResource(id = R.string.theme_info)
                ) {
                    mainNavController.navigate(ScreenDestination.Theme)
                }
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.Source,
                    title = stringResource(id = R.string.media_lib),
                    subTitle = stringResource(id = R.string.scan_music)
                ) {
                    mainNavController.navigate(ScreenDestination.MediaLibs)
                }
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.FontDownload,
                    title = stringResource(id = R.string.lyric),
                    subTitle = stringResource(id = R.string.lyric_info)
                ) {
                    mainNavController.navigate(ScreenDestination.Lyric)
                }
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.Science,
                    title = stringResource(id = R.string.lab),
                    subTitle = stringResource(id = R.string.lab)
                ) {
                    mainNavController.navigate(ScreenDestination.Lab)
                }
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.Info,
                    title = stringResource(id = R.string.about),
                    subTitle = stringResource(
                        id = R.string.about_info,
                        VersionUtils.VersionName
                    )
                ) {
                    mainNavController.navigate(ScreenDestination.About)
                }
            }
        }
    }
}