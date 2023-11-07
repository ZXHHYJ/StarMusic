package com.zxhhyj.music.ui.screen.pro

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
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.theme.ColorScheme
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun ProScreen(paddingValues: PaddingValues, mainNavController: NavController<ScreenDestination>) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.star_music_pro)
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                        }
                    )
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
                                imageVector = Icons.Rounded.LibraryMusic,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(text = stringResource(id = R.string.cue_support))
                        },
                        subText = {
                            Text(text = stringResource(id = R.string.cue_support_info))
                        },
                        checked = SettingRepository.EnableCueSupport,
                        onCheckedChange = {
                            SettingRepository.EnableCueSupport = it
                        },
                        enabled = SettingRepository.EnableStarMusicPro,
                    )
                    ItemDivider()
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.CloudUpload,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.webdav)) },
                        subText = { },
                        enabled = SettingRepository.EnableStarMusicPro
                    ) {
                        mainNavController.navigate(ScreenDestination.WebDavConfig)
                    }
                }
            }
            item {
                ItemSpacer()
            }
            if (SettingRepository.EnableStarMusicPro) {
                item {
                    if (SettingRepository.EnableHonestPayment) {
                        RoundColumn(modifier = Modifier.fillMaxWidth()) {
                            ItemArrowRight(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.thank_you_support)) },
                                subText = {}) {
                                mainNavController.navigate(ScreenDestination.Pay)
                            }
                        }
                    } else {
                        CompositionLocalProvider(
                            LocalColorScheme provides ColorScheme(
                                highlight = Color.White,
                                highBackground = LocalColorScheme.current.highlight,
                                text = Color.White,
                                subText = Color.White
                            )
                        ) {
                            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                                ItemArrowRight(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Rounded.ShoppingCart,
                                            contentDescription = null
                                        )
                                    },
                                    text = { Text(text = stringResource(id = R.string.buy_star_music_pro)) },
                                    subText = {}) {
                                    mainNavController.navigate(ScreenDestination.Pay)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}