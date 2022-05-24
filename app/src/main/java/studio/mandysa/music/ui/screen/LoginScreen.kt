package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.KenBurns
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.translucentWhite
import studio.mandysa.music.ui.theme.verticalMargin
import studio.mandysa.music.ui.viewmodel.EventViewModel


@Composable
@Preview
private fun LoginDialog() {
    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .height(100.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(event: EventViewModel = viewModel()) {
    val textFileColors = textFieldColors(
        textColor = Color.Black,
        containerColor = translucentWhite,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
    val loginStatus by event.loginStatus.observeAsState()
    when (loginStatus) {
        is EventViewModel.Status.LoggingIn -> {
            LoginDialog()
        }
        is EventViewModel.Status.Fail -> {

        }
        is EventViewModel.Status.Ok -> {

        }
        is EventViewModel.Status.Error -> {

        }
        else -> {}
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
        KenBurns(
            modifier = Modifier.fillMaxSize(),
            imageUrl = "https://i.hexuexiao.cn/up/b1/c3/29/cc0ec73b1b1d8d4cbbfed2297d29c3b1.jpg",
            paused = false
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalMargin, horizontal = horizontalMargin),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color.White
            )
            var phone by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                placeholder = { Text(stringResource(R.string.phone)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = verticalMargin),
                onValueChange = {
                    phone = it
                },
                colors = textFileColors
            )
            OutlinedTextField(
                value = password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { Text(stringResource(R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    password = it
                }, colors = textFileColors
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.size(60.dp),
                onClick = {
                    if (phone.isEmpty() || password.isEmpty()) {
                        // TODO: 处理一下手机号或密码没有填写的情况
                        return@Button
                    }
                    event.login(mobilePhone = phone, password = password)
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
    }
}