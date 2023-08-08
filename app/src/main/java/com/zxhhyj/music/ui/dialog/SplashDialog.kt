package com.zxhhyj.music.ui.dialog

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.PrivacyPolicyURL
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.ActivityUtils
import com.zxhhyj.ui.view.YesNoDialog
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun SplashDialog(onDismissRequest: () -> Unit) {
    val activity = LocalContext.current as Activity
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.privacy_policy),
        confirm = {
            Text(
                text = stringResource(id = R.string.agree_and_continue),
                modifier = Modifier.clickable {
                    onDismissRequest.invoke()
                    SettingRepository.AgreePrivacyPolicy = true
                })
        },
        dismiss = {
            Text(text = stringResource(id = R.string.cancel), modifier = Modifier.clickable {
                onDismissRequest.invoke()
                activity.finish()
            })
        })
    {
        Text(text = buildAnnotatedString {
            append(stringResource(id = R.string.splash_dialog_text))
            withStyle(
                style = SpanStyle(
                    color = LocalColorScheme.current.highlight,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(stringResource(id = R.string.privacy_policy))
            }
        }, modifier = Modifier.clickable {
            ActivityUtils.openWeb(activity, PrivacyPolicyURL)
        })
    }
}