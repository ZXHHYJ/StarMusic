package com.zxhhyj.music.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.BiliBiliHome
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.ActivityUtils
import com.zxhhyj.music.ui.theme.bilibiliColor
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.YesNoDialog

@Composable
fun BiliBiliPowerDialog(onDismissRequest: () -> Unit) {
    CompositionLocalProvider(LocalColorScheme provides LocalColorScheme.current.copy(highlight = bilibiliColor)) {
        YesNoDialog(
            onDismissRequest = onDismissRequest,
            title = stringResource(id = R.string.bilibili_power),
            confirm = {
                Text(
                    text = stringResource(id = R.string.i_have_made_an_honest_payment),
                    modifier = Modifier.clickable {
                        SettingRepository.EnableStarMusicPro = true
                        onDismissRequest.invoke()
                    })
            },
            dismiss = {
                Text(
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier.clickable { onDismissRequest.invoke() })
            }) {
            Text(text = stringResource(id = R.string.buy_star_music_pro_introduction))
        }
        val ctx = LocalContext.current
        LaunchedEffect(Unit) {
            ActivityUtils.openUrl(ctx, BiliBiliHome)
        }
    }
}