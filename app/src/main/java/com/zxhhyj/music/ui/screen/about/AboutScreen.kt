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
import com.zxhhyj.music.ui.common.topbar.TopAppBar
import com.zxhhyj.music.ui.common.topbar.bindTopAppBarState
import com.zxhhyj.music.ui.common.topbar.rememberTopAppBarState
import com.zxhhyj.music.ui.item.SubTitleItem
import com.zxhhyj.music.ui.screen.setting.item.SettingItem


@Composable
fun AboutScreen(
    padding: PaddingValues,
) {
    val ctx = LocalContext.current
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .bindTopAppBarState(topAppBarState)
    ) {
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
    }
    TopAppBar(
        state = topAppBarState, modifier = Modifier, title = stringResource(id = R.string.about)
    )
}