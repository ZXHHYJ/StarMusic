package studio.mandysa.music.ui.screen.cloud.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.theme.*


@Preview
@Composable
private fun LoginDialog() {
    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        AppDialog {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(dialogBackground)
                    .height(120.dp), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun PreviewLoginScreen() {
    LoginScreen()
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun LoginScreen(cloudLoginViewModel: CloudLoginViewModel = viewModel()) {
    val loginNavController =
        rememberNavController<CloudLoginDestination>(startDestination = CloudLoginDestination.Phone)
    val keyboardController = LocalSoftwareKeyboardController.current
    val textFileColors = textFieldColors(
        textColor = Color.Black,
        containerColor = translucentWhite,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
    val status by cloudLoginViewModel.dialogState.observeAsState(false)
    if (status) {
        keyboardController?.hide()
        LoginDialog()
    }
    rememberSystemUiController().apply {
        setSystemBarsColor(
            Color.Transparent,
            false,
            isNavigationBarContrastEnforced = false
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MotionBlur(
            modifier = Modifier.fillMaxSize(),
            url = "https://i.hexuexiao.cn/up/b1/c3/29/cc0ec73b1b1d8d4cbbfed2297d29c3b1.jpg",
            paused = false
        )
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .padding(vertical = verticalMargin, horizontal = horizontalMargin),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color.White
            )
            NavHost(loginNavController) { screenDestination ->
                when (screenDestination) {
                    CloudLoginDestination.Phone -> {
                        var phone by rememberSaveable { mutableStateOf("") }
                        var captcha by rememberSaveable { mutableStateOf("") }
                        val captchaSecond by cloudLoginViewModel.sendCaptchaSecond.observeAsState(0)
                        Box(contentAlignment = Alignment.CenterEnd) {
                            OutlinedTextField(
                                value = phone, singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                placeholder = { Text(stringResource(R.string.phone)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = verticalMargin),
                                onValueChange = {
                                    if (it.length <= 11) {
                                        phone = it
                                    }
                                },
                                colors = textFileColors
                            )
                            Text(text = if (captchaSecond == 0) stringResource(R.string.send_captcha) else "$captchaSecond",
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clickable {
                                        cloudLoginViewModel.sendCaptcha(phone)
                                    })
                        }
                        OutlinedTextField(
                            value = captcha, singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            placeholder = { Text(stringResource(R.string.captcha)) },
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                if (it.length <= 4) {
                                    captcha = it
                                }
                            }, colors = textFileColors
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            modifier = Modifier.size(60.dp),
                            onClick = {
                                if (phone.isEmpty() || captcha.isEmpty()) {
                                    // TODO: 处理一下手机号或验证码没有填写的情况
                                    return@Button
                                }
                                cloudLoginViewModel.mobileLogin(mobilePhone = phone, captcha = captcha)
                            },
                            elevation = null,
                            colors = buttonColors(backgroundColor = translucentWhite),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Icon(
                                modifier = Modifier.rotate(180f),
                                imageVector = Icons.Rounded.ArrowBackIos,
                                contentDescription = null
                            )
                        }
                    }
                    CloudLoginDestination.QRCode -> {
                        val qrBitmap by cloudLoginViewModel.qrBitmapLiveData.observeAsState(null)

                        if (qrBitmap == null)
                            cloudLoginViewModel.refreshQr()

                        Card(
                            modifier = Modifier
                                .size(250.dp)
                                .drawWithContent {
                                    drawContent()
                                },
                            colors = CardDefaults.cardColors(containerColor = translucentWhite),
                        ) {
                            qrBitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp),
                            onClick = {
                                cloudLoginViewModel.refreshQr()
                            },
                            elevation = null,
                            colors = buttonColors(backgroundColor = translucentWhite),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(text = "刷新")
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                if (loginNavController.backstack.entries.last().destination == CloudLoginDestination.Phone) {
                    loginNavController.navigate(CloudLoginDestination.QRCode)
                } else {
                    loginNavController.pop()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            elevation = null,
            colors = buttonColors(backgroundColor = translucentWhite),
            shape = roundedCornerShape
        ) {
            Text(text = "切换")
        }
    }
}