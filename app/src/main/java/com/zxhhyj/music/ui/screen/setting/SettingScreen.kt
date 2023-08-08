package com.zxhhyj.music.ui.screen.setting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.Scaffold
import com.zxhhyj.ui.view.TopBar
import com.zxhhyj.ui.view.item.Item
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SettingScreen(
    mainNavController: NavController<ScreenDestination>,
    padding: PaddingValues,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.setting)
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.ColorLens, null) },
                    text = { Text(text = stringResource(id = R.string.theme)) },
                    subText = { Text(text = stringResource(id = R.string.theme_info)) }) {
                    mainNavController.navigate(ScreenDestination.Theme)
                }
            }
            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Source, null) },
                    text = { Text(text = stringResource(id = R.string.media_lib)) },
                    subText = { Text(text = stringResource(id = R.string.scan_music)) }) {
                    mainNavController.navigate(ScreenDestination.MediaLibs)
                }
            }
            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.FontDownload, null) },
                    text = { Text(text = stringResource(id = R.string.lyric)) },
                    subText = { Text(text = stringResource(id = R.string.scan_music)) }) {
                    mainNavController.navigate(ScreenDestination.Lyric)
                }
            }
            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Science, null) },
                    text = { Text(text = stringResource(id = R.string.lab)) },
                    subText = { Text(text = stringResource(id = R.string.lab)) }) {
                    mainNavController.navigate(ScreenDestination.Lab)
                }
            }
            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Info, null) },
                    text = { Text(text = stringResource(id = R.string.about)) },
                    subText = {
                        Text(
                            text = stringResource(
                                id = R.string.about_info,
                                VersionUtils.VersionName
                            )
                        )
                    }) {
                    mainNavController.navigate(ScreenDestination.About)
                }
            }
        }
    }
}