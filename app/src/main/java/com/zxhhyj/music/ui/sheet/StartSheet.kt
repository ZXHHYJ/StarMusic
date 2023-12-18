package com.zxhhyj.music.ui.sheet

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.config.PrivacyPolicyURL
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.ActivityUtils
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.theme.ColorScheme
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSubTitle
import com.zxhhyj.ui.view.item.ItemTint
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop

@Composable
fun StartSheet(sheetNavController: NavController<SheetDestination>) {
    val activity = LocalContext.current as Activity?
    BackHandler {
        activity?.finish()
    }
    LazyColumn(modifier = Modifier.background(LocalColorScheme.current.background)) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.StarRate,
                    contentDescription = null,
                    tint = LocalColorScheme.current.highlight,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColorScheme.current.text
                )
            }
        }
        item { ItemSpacer() }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                ItemTint {
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
                        if (activity != null) {
                            ActivityUtils.openUrl(activity, PrivacyPolicyURL)
                        }
                    })
                }
            }
        }
        item { ItemSpacer() }
        item {
            ItemSubTitle {
                Text(text = stringResource(id = R.string.request_permission))
            }
        }
        item {
            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                Item(text = { Text(text = stringResource(id = R.string.network_connection_permission)) },
                    subText = { Text(text = stringResource(id = R.string.network_connection_permission_info)) }) {

                }
                ItemDivider()
                Item(text = { Text(text = stringResource(id = R.string.read_internal_storage_permission)) },
                    subText = { Text(text = stringResource(id = R.string.read_internal_storage_permission_info)) }) {

                }
                ItemDivider()
                Item(text = { Text(text = stringResource(id = R.string.post_notifications_permission)) },
                    subText = { Text(text = stringResource(id = R.string.post_notifications_permission_info)) }) {

                }
            }
        }
        item { ItemSpacer() }
        item {
            CompositionLocalProvider(
                LocalColorScheme provides ColorScheme(
                    highlight = Color.White,
                    highBackground = LocalColorScheme.current.highlight,
                    text = Color.White,
                    subText = Color.White
                )
            ) {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(icon = {
                        Icon(
                            imageVector = Icons.Rounded.Check, contentDescription = null
                        )
                    },
                        text = { Text(text = stringResource(id = R.string.read_and_agree)) },
                        subText = { }) {
                        SettingRepository.AgreePrivacyPolicy = true
                        sheetNavController.pop()
                    }
                }
            }
        }
        item { ItemSpacer() }
    }
}
