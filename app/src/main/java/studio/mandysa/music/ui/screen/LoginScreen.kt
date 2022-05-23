package studio.mandysa.music.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R
import studio.mandysa.music.ui.dialog.LoginDialog
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.verticalMargin
import studio.mandysa.music.ui.viewmodel.EventViewModel

@Composable
fun LoginScreen(navController: NavHostController, event: EventViewModel = viewModel()) {
    val loginStatus = event.loginStatus.observeAsState()
    when (loginStatus.value) {
        is EventViewModel.Status.LoggingIn -> {
            LoginDialog()
        }
        is EventViewModel.Status.Fail -> {

        }
        is EventViewModel.Status.Ok -> {
            navController.navigate(MainActivity.NavScreen.Main.route) {
                launchSingleTop = true
            }
        }
        is EventViewModel.Status.Error -> {

        }
        else -> {}
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            var phone by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = verticalMargin, horizontal = horizontalMargin)
            ) {
                OutlinedTextField(
                    value = phone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    placeholder = { Text(stringResource(R.string.phone)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = verticalMargin),
                    onValueChange = {
                        phone = it
                    })
                OutlinedTextField(
                    value = password,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    placeholder = { Text(stringResource(R.string.password)) },
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        password = it
                    })
            }
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.primary)
                    .navigationBarsPadding(),
                onClick = {
                    event.login(mobilePhone = phone, password = password)
                }) {
                Text(stringResource(R.string.login), color = Color.White)
            }
        }
    }
}