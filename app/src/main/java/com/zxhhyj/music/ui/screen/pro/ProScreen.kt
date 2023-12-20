package com.zxhhyj.music.ui.screen.pro

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.common.VipIcon
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemSubTitle
import com.zxhhyj.ui.view.item.ItemSwitcher
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun ProScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>
) {
    if (!SettingRepository.EnableStarMusicPro) {
        DisposableEffect(Unit) {
            onDispose {
                SettingRepository.EnableWebDav = false
            }
        }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.star_music_pro)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.StarRate,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.star_music_pro)) },
                        subText = { },
                        checked = SettingRepository.EnableStarMusicPro,
                        onCheckedChange = {
                            SettingRepository.EnableStarMusicPro = it
                            if (it) {
                                mainNavController.navigate(ScreenDestination.Pay)
                            }
                        }
                    )
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.pro_function))
                }
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
                        subText = { VipIcon() },
                        enabled = SettingRepository.EnableStarMusicPro
                    ) {
                        mainNavController.navigate(ScreenDestination.WebDavConfig)
                    }
                }
            }
        }
    }
}
