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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import studio.mandysa.music.R
import studio.mandysa.music.ui.theme.Blue40
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.verticalMargin
import studio.mandysa.music.ui.viewmodel.EventViewModel

@Composable
@Preview
fun LoginScreen(event: EventViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            var phone by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
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
                    .background(Blue40)
                    .navigationBarsPadding(),
                onClick = {
                    event.login(mobilePhone = phone, password = password)
                }) {
                Text(stringResource(R.string.login), color = Color.White)
            }
        }
    }
}