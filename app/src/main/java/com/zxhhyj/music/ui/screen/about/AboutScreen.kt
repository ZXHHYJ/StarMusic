package com.zxhhyj.music.ui.screen.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.BiliBiliHome
import com.zxhhyj.music.logic.config.PrivacyPolicyURL
import com.zxhhyj.music.logic.utils.ActivityUtils
import com.zxhhyj.music.logic.utils.VersionUtils
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSubTitle


@Composable
fun AboutScreen(
    paddingValues: PaddingValues,
) {
    val ctx = LocalContext.current
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.about)) })
        })
    {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.version)) },
                        subText = { Text(text = VersionUtils.VersionName) }) {
                        //不需要反馈
                    }
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.about_link))
                }
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.privacy_policy)) },
                        subText = { }) {
                        ActivityUtils.openUrl(ctx, PrivacyPolicyURL)
                    }
                    ItemDivider()
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = "Gitee") },
                        subText = { }) {
                        ActivityUtils.openUrl(ctx, "https://gitee.com/ZXHHYJ/star_music")
                    }
                    ItemDivider()
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = "Github") },
                        subText = { }) {
                        ActivityUtils.openUrl(ctx, "https://github.com/ZXHHYJ/StarMusic")
                    }
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.contact_label))
                }
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.mail)) },
                        subText = { }) {
                        ActivityUtils.openMail(ctx, "957447668@qq.com")
                    }
                    ItemDivider()
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = "BiliBili") },
                        subText = { }) {
                        ActivityUtils.openUrl(ctx, BiliBiliHome)
                    }
                    ItemDivider()
                    ItemArrowRight(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.qq_group)) },
                        subText = { }) {
                        ActivityUtils.openQQGroup(ctx, "Cd-49roM1cv_k39_CJmsBh_J3AS9m3Mz")
                    }
                }
            }
        }
    }
}