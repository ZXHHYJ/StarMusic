package com.zxhhyj.music.ui.screen.lab

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Science
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun LabScreen(
    paddingValues: PaddingValues
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.lab)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Science,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.e_ink_screen_mode)) },
                        subText = { },
                        checked = SettingRepository.EnableLinkUI,
                        onCheckedChange = {
                            SettingRepository.EnableLinkUI = it
                        }
                    )
                }
            }
        }
    }
}