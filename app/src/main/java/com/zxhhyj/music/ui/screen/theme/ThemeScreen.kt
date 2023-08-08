package com.zxhhyj.music.ui.screen.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.Scaffold
import com.zxhhyj.ui.TopBar
import com.zxhhyj.ui.item.ItemSwitcher

@Composable
fun ThemeScreen(padding: PaddingValues) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = { TopBar(modifier = Modifier, title = stringResource(id = R.string.theme)) }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ItemSwitcher(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.ColorLens,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(text = "启用新播放器UI(Beta)")
                    },
                    subText = {
                        Text(text = "从Apple Music上抄过来的")
                    },
                    checked = SettingRepository.EnableNewPlayerUI,
                    onCheckedChange = {
                        SettingRepository.EnableNewPlayerUI = it
                    }
                )
            }
        }
    }
}