package com.zxhhyj.music.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
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
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SettingScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues,
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColorScheme.current.subBackground)
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.setting)
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(imageVector = Icons.Filled.StarRate, null)
                        },
                        text = { Text(text = stringResource(id = R.string.star_music_pro)) },
                        subText = { Text(text = "已禁用") }) {
                        mainNavController.navigate(ScreenDestination.Vip)
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = { Icon(imageVector = Icons.Rounded.Source, null) },
                        text = { Text(text = stringResource(id = R.string.media_lib)) },
                        subText = { Text(text = stringResource(id = R.string.scan_music)) }) {
                        mainNavController.navigate(ScreenDestination.MediaLibs)
                    }
                    ItemDivider()
                    Item(
                        icon = { Icon(imageVector = Icons.Rounded.FontDownload, null) },
                        text = { Text(text = stringResource(id = R.string.lyric)) },
                        subText = { Text(text = stringResource(id = R.string.lyric)) }) {
                        mainNavController.navigate(ScreenDestination.Lyric)
                    }
                    ItemDivider()
                    Item(
                        icon = { Icon(imageVector = Icons.Rounded.Science, null) },
                        text = { Text(text = stringResource(id = R.string.lab)) },
                        subText = { Text(text = stringResource(id = R.string.lab)) }) {
                        mainNavController.navigate(ScreenDestination.Lab)
                    }
                    ItemDivider()
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
}