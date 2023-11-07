package com.zxhhyj.music.ui.screen.webdavconfig

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.NetworkWifi
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phonelink
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
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
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.launch


@Composable
fun WebDavConfigScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier, title = stringResource(id = R.string.webdav)
            )
        }) {
        Column(modifier = Modifier.fillMaxSize()) {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                ItemSwitcher(
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
                    checked = SettingRepository.EnableWebDav,
                    onCheckedChange = {
                        SettingRepository.EnableWebDav = it
                    }
                )
            }
            ItemSpacer()
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                ItemArrowRight(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Phonelink,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.webdav_address)) },
                    subText = {
                        Text(text = SettingRepository.WebDavConfig.address.takeIf { it.isNotEmpty() }
                            ?: stringResource(id = R.string.nothing))
                    },
                    enabled = SettingRepository.EnableWebDav
                ) {
                    dialogNavController.navigate(DialogDestination.EditWebDavAddress)
                }
                ItemDivider()
                ItemArrowRight(
                    icon = { Icon(imageVector = Icons.Rounded.Person, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.webdav_user)) },
                    subText = {
                        Text(text = SettingRepository.WebDavConfig.username.takeIf { it.isNotEmpty() }
                            ?: stringResource(id = R.string.nothing))
                    },
                    enabled = SettingRepository.EnableWebDav
                ) {
                    dialogNavController.navigate(DialogDestination.EditWebDavUsername)
                }
                ItemDivider()
                ItemArrowRight(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Password,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.webdav_password)) },
                    subText = {
                        Text(text = SettingRepository.WebDavConfig.password.takeIf { it.isNotEmpty() }
                            ?: stringResource(id = R.string.nothing))
                    },
                    enabled = SettingRepository.EnableWebDav
                ) {
                    dialogNavController.navigate(DialogDestination.EditWebDavPassword)
                }
            }
            ItemSpacer()
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                val lifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope
                Item(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.NetworkWifi,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.test_webdav_link)) },
                    subText = { },
                    enabled = SettingRepository.EnableWebDav
                ) {
                    lifecycleCoroutineScope.launch {
                        WebDavMediaLibRepository.testLink()
                    }
                }
                ItemDivider()
                Item(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Sync,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.sync_webdav)) },
                    subText = { },
                    enabled = SettingRepository.EnableWebDav
                ) {
                    mainNavController.navigate(ScreenDestination.WebDav)
                    dialogNavController.navigate(DialogDestination.SyncWebDavMediaLib)
                }
            }
        }
    }
}

