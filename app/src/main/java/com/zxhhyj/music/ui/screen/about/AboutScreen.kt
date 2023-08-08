package com.zxhhyj.music.ui.screen.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.zxhhyj.ui.item.Item


@Composable
fun AboutScreen(
    padding: PaddingValues,
) {
    val ctx = LocalContext.current
    com.zxhhyj.ui.Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            com.zxhhyj.ui.TopBar(
                modifier = Modifier,
                title = stringResource(id = R.string.about)
            )
        })
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.version)) },
                    subText = { Text(text = VersionUtils.VersionName) }) {
                    //不需要反馈
                }
            }

            item {
                SubTitleItem(title = stringResource(id = R.string.link))
            }

            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = null) },
                    text = { Text(text = "Gitee") },
                    subText = { }) {
                    ActivityUtils.openWeb(ctx, "https://gitee.com/ZXHHYJ/star_music")
                }
            }

            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.privacy_policy)) },
                    subText = { }) {
                    ActivityUtils.openWeb(ctx, PrivacyPolicyURL)
                }
            }
            item {
                Item(
                    icon = { Icon(imageVector = Icons.Rounded.Link, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.mail)) },
                    subText = { }) {
                    ActivityUtils.openMail(ctx, "957447668@qq.com")
                }
            }
        }
    }
}