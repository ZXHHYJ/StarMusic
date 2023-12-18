package com.zxhhyj.music.ui.screen.pro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.common.VipIcon
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.theme.wechatColor
import com.zxhhyj.ui.theme.ColorScheme
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSwitcher
import com.zxhhyj.ui.view.item.ItemTint
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
                SettingRepository.EnableCueSupport = false
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
            when (SettingRepository.EnableStarMusicPro) {
                true -> {
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
                            ItemTint {
                                Text(text = stringResource(id = R.string.membership_function))
                            }
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
                                        imageVector = Icons.Rounded.AudioFile,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.cue_support)) },
                                subText = { VipIcon() }) {
                                mainNavController.navigate(ScreenDestination.MediaLibConfig)
                            }
                            ItemDivider()
                            ItemArrowRight(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.CloudUpload,
                                        contentDescription = null
                                    )
                                },
                                text = { Text(text = stringResource(id = R.string.webdav)) },
                                subText = { VipIcon() }) {
                                mainNavController.navigate(ScreenDestination.WebDavConfig)
                            }
                        }
                    }
                }

                false -> {
                    item {
                        RoundColumn(modifier = Modifier.fillMaxWidth()) {
                            ItemTint {
                                Text(
                                    text = buildAnnotatedString {
                                        append(stringResource(id = R.string.star_music_pro_member) + "\n")
                                        withStyle(
                                            style = SpanStyle(
                                                color = LocalColorScheme.current.text,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 28.sp
                                            )
                                        ) {
                                            append("￥2.0")
                                        }
                                    }
                                )
                            }
                        }
                    }
                    item {
                        ItemSpacer()
                    }
                    item {
                        RoundColumn(modifier = Modifier.fillMaxWidth()) {
                            ItemTint {
                                Text(text = stringResource(id = R.string.membership_function))
                            }
                        }
                    }
                    item {
                        ItemSpacer()
                    }
                    item {
                        CompositionLocalProvider(
                            LocalColorScheme provides ColorScheme(
                                highlight = Color.White,
                                highBackground = wechatColor,
                                text = Color.White,
                                subText = Color.White
                            )
                        ) {
                            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                                ItemArrowRight(
                                    icon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.icons8_wechat),
                                            contentDescription = null
                                        )
                                    },
                                    text = { Text(text = stringResource(id = R.string.wechat_payment)) },
                                    subText = {}) {
                                    mainNavController.navigate(ScreenDestination.Pay)
                                }
                            }
                        }
                    }
                    item {
                        ItemSpacer()
                    }
                    item {
                        RoundColumn(modifier = Modifier.fillMaxWidth()) {
                            ItemTint {
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                            append(stringResource(id = R.string.previously_purchased))
                                        }
                                    },
                                    Modifier.clickable {
                                        SettingRepository.EnableStarMusicPro = true
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}