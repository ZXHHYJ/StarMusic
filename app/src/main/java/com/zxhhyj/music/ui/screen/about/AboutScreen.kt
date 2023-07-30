package com.zxhhyj.music.ui.screen.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.zxhhyj.music.ui.common.AppScaffold
import com.zxhhyj.music.ui.common.AppTopBar
import com.zxhhyj.music.ui.item.SettingItem
import com.zxhhyj.music.ui.item.SubTitleItem


@Composable
fun AboutScreen(
    padding: PaddingValues,
) {
    val ctx = LocalContext.current
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = { AppTopBar(modifier = Modifier, title = stringResource(id = R.string.about)) })
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                SettingItem(
                    imageVector = Icons.Rounded.Info,
                    title = stringResource(id = R.string.version),
                    subTitle = VersionUtils.VersionName
                ) {
                    //无需反馈
                }
            }

            item {
                SubTitleItem(title = stringResource(id = R.string.link))
            }

            item {
                SettingItem(
                    imageVector = Icons.Rounded.Link,
                    title = "Gitee"
                ) {
                    ActivityUtils.openWeb(ctx, "https://gitee.com/ZXHHYJ/star_music")
                }
            }

            item {
                SettingItem(
                    imageVector = Icons.Rounded.Link,
                    title = stringResource(id = R.string.privacy_policy)
                ) {
                    ActivityUtils.openWeb(ctx, PrivacyPolicyURL)
                }
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.Link,
                    title = stringResource(id = R.string.mail)
                ) {
                    ActivityUtils.openMail(ctx, "957447668@qq.com")
                }
            }
        }
    }
}