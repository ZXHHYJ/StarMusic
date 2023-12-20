package com.zxhhyj.music.ui.screen.wechatpay

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.theme.wechatColor
import com.zxhhyj.ui.theme.ColorScheme
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemTint
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop

@Composable
fun WeChatPayScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.wechat_payment)) })
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            item {
                RoundColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.weixin_pay),
                            modifier = Modifier
                                .sizeIn(maxWidth = 300.dp, maxHeight = 300.dp)
                                .fillMaxSize()
                                .align(Alignment.Center),
                            contentDescription = null
                        )
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemTint {
                        Text(text = stringResource(id = R.string.buy_star_music_pro_introduction))
                    }
                }
            }
            item {
                ItemSpacer()
            }
            item {
                CompositionLocalProvider(
                    LocalColorScheme provides ColorScheme(
                        highlight = Color.White,
                        highBackground = wechatColor,
                        text = Color.White
                    )
                ) {
                    RoundColumn(modifier = Modifier.fillMaxWidth()) {
                        Item(
                            icon = {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(text = buildAnnotatedString {
                                    append(stringResource(id = R.string.i_have_made_an_honest_payment))
                                    withStyle(
                                        style = SpanStyle(
                                            color = LocalColorScheme.current.text,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append("￥2.0")
                                    }
                                })
                            },
                            subText = { }) {
                            SettingRepository.EnableStarMusicPro = true
                            mainNavController.pop()
                        }
                    }
                }
            }
        }
    }
}