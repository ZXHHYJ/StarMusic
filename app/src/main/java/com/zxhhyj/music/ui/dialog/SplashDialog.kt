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
import androidx.compose.ui.window.DialogProperties
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.PrivacyPolicyURL
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.ActivityUtils
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.YesNoDialog
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SplashDialog(
    onDismissRequest: () -> Unit,
    dialogNavController: NavController<DialogDestination>
) {
    val activity = LocalContext.current as Activity
    YesNoDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = stringResource(id = R.string.privacy_policy),
        confirm = {
            Text(
                text = stringResource(id = R.string.agree_and_continue),
                modifier = Modifier.clickable {
                    onDismissRequest.invoke()
                    SettingRepository.AgreePrivacyPolicy = true
                    if (AndroidMediaLibsRepository.songs.isEmpty()) {
                        dialogNavController.navigate(DialogDestination.MediaLibsEmpty)
                    }
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
        }, color = LocalColorScheme.current.text, modifier = Modifier.clickable {
            ActivityUtils.openWeb(activity, PrivacyPolicyURL)
        })
    }
}