package com.zxhhyj.music.ui.screen.privacypolicy

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.PrivacyPolicyURL
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.ActivityUtils
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemTint
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop

@Composable
fun PrivacyPolicyScreen(mainNavController: NavController<ScreenDestination>) {
    BackHandler {
        //拦截返回键
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColorScheme.current.background)
            .systemBarsPadding()
            .clickable(false) { }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            tint = LocalColorScheme.current.highlight,
            modifier = Modifier.align(Alignment.Center)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                ItemTint {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(id = R.string.splash_dialog_text))
                            withStyle(
                                style = SpanStyle(
                                    color = LocalColorScheme.current.highlight,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(stringResource(id = R.string.privacy_policy))
                            }
                        }, color = LocalColorScheme.current.text
                    )
                }
                ItemDivider()
                val activity = LocalContext.current as Activity?
                ItemArrowRight(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.PrivacyTip,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.privacy_policy)) },
                    subText = { }) {
                    if (activity != null) {
                        ActivityUtils.openUrl(activity, PrivacyPolicyURL)
                    }
                }
            }
            ItemSpacer()
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                ItemArrowRight(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.read_and_agree)) },
                    subText = { }) {
                    SettingRepository.AgreePrivacyPolicy = true
                    mainNavController.pop()
                }
            }
            ItemSpacer()
        }
    }
}