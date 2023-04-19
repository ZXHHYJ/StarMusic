package com.zxhhyj.music.ui.dialog

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.zxhhyj.music.ui.common.AppDialog
import com.zxhhyj.music.ui.theme.appTextAccentColor


@Composable
fun SplashDialog(onDismissRequest: () -> Unit) {
    val activity = LocalContext.current as? Activity
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.privacy_policy),
        confirm = {
            Text(
                text = stringResource(id = R.string.agree_and_continue),
                modifier = Modifier.clickable {
                    onDismissRequest.invoke()
                })
        },
        dismiss = {
            Text(text = stringResource(id = R.string.cancel), modifier = Modifier.clickable {
                onDismissRequest.invoke()
                activity?.finish()
            })
        })
    {

        Text(text = buildAnnotatedString {
            append(stringResource(id = R.string.splash_dialog_text))
            withStyle(style = SpanStyle(color = appTextAccentColor, fontWeight = FontWeight.Bold)) {
                append(stringResource(id = R.string.privacy_policy))
            }
        }, modifier = Modifier.clickable {
            val uri = Uri.parse("https://www.bing.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity?.startActivity(intent)
        })
    }
}