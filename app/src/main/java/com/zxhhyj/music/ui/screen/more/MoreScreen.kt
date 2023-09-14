package com.zxhhyj.music.ui.screen.more

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
import androidx.compose.material.icons.rounded.MiscellaneousServices
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Source
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
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
fun MoreScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues,
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.more)
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
                        subText = {
                            when (SettingRepository.EnableStarMusicPro) {
                                true -> Text(
                                    text = stringResource(id = R.string.slogo),
                                    color = LocalColorScheme.current.highlight
                                )

                                false -> Text(text = stringResource(id = R.string.slogo))
                            }
                        }) {
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
                        icon = { Icon(imageVector = Icons.Rounded.Palette, null) },
                        text = { Text(text = stringResource(id = R.string.personalize)) },
                        subText = { Text(text = stringResource(id = R.string.personalize)) }) {
                        mainNavController.navigate(ScreenDestination.Personalize)
                    }
                    ItemDivider()
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
                        icon = { Icon(imageVector = Icons.Rounded.MiscellaneousServices, null) },
                        text = { Text(text = stringResource(id = R.string.misc)) },
                        subText = { Text(text = stringResource(id = R.string.misc)) }) {
                        mainNavController.navigate(ScreenDestination.Misc)
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