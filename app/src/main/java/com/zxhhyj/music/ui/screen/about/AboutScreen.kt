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
import com.zxhhyj.music.logic.helper.ActivityHelper
import com.zxhhyj.music.logic.helper.VersionHelper
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
                    subTitle = VersionHelper.VersionName
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
                    ActivityHelper.openWeb(ctx, "https://gitee.com/ZXHHYJ/star_music")
                }
            }

            item {
                SettingItem(
                    imageVector = Icons.Rounded.Link,
                    title = stringResource(id = R.string.privacy_policy)
                ) {
                    //无需反馈
                }
            }
            item {
                SettingItem(
                    imageVector = Icons.Rounded.Link,
                    title = stringResource(id = R.string.mail)
                ) {
                    ActivityHelper.openMail(ctx, "957447668@qq.com")
                }
            }
        }
    }
}