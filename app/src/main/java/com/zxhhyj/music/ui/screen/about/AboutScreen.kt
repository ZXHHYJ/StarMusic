package com.zxhhyj.music.ui.screen.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.zxhhyj.music.logic.config.PrivacyPolicyURL
import com.zxhhyj.music.logic.utils.ActivityUtils
import com.zxhhyj.music.logic.utils.VersionUtils
import com.zxhhyj.music.ui.item.SubTitleItem
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemDivider


@Composable
fun AboutScreen(
    paddingValues: PaddingValues,
) {
    val ctx = LocalContext.current
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier,
                title = stringResource(id = R.string.about)
            )
        })
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                SubTitleItem(title = stringResource(id = R.string.link))
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = "Gitee") },
                        subText = { }) {
                        ActivityUtils.openWeb(ctx, "https://gitee.com/ZXHHYJ/star_music")
                    }
                    ItemDivider()
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = "Github") },
                        subText = { }) {
                        ActivityUtils.openWeb(ctx, "https://github.com/ZXHHYJ/StarMusic")
                    }
                    ItemDivider()
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.privacy_policy)) },
                        subText = { }) {
                        ActivityUtils.openWeb(ctx, PrivacyPolicyURL)
                    }
                    ItemDivider()
                    Item(
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
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = "BiliBili") },
                        subText = { }) {
                        ActivityUtils.openWeb(ctx, "https://b23.tv/LT3jgMj")
                    }
                    ItemDivider()
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.douyin)) },
                        subText = { }) {
                        ActivityUtils.openWeb(ctx, " https://v.douyin.com/idYBDbyr/")
                    }
                }
            }
        }
    }
}