package com.zxhhyj.music.ui.screen.webdavconfig

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate


@Composable
fun WebDavConfigScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.webdav)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Create,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.add_webdav)) },
                        subText = { },
                    ) {
                        mainNavController.navigate(
                            ScreenDestination.WebDavEdit(
                                WebDavMediaLibRepository.createWebSource()
                            )
                        )
                    }
                }
            }
            item {
                ItemSpacer()
            }
            roundColumn {
                items(WebDavMediaLibRepository.WebDavSources) {
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Storage,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(
                                text = it.remark.takeIf { it.isNotEmpty() }
                                    ?: stringResource(id = R.string.nothing)
                            )
                        },
                        subText = {
                            Text(text = it.username.takeIf { it.isNotEmpty() }
                                ?: stringResource(id = R.string.nothing))
                        }) {
                        mainNavController.navigate(
                            ScreenDestination.WebDavEdit(
                                WebDavMediaLibRepository.WebDavSources.indexOf(it)
                            )
                        )
                    }
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
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.refresh_webdav_media_lib)) },
                        subText = { },
                        enabled = SettingRepository.EnableWebDav && WebDavMediaLibRepository.WebDavSources.isNotEmpty() && WebDavMediaLibRepository.WebDavSources.any { it.folders.isNotEmpty() }
                    ) {
                        dialogNavController.navigate(DialogDestination.RefreshWebDavMediaLib)
                    }
                }
            }
        }
    }
}

