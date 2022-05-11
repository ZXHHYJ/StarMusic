package studio.mandysa.music.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import studio.mandysa.music.R
import studio.mandysa.music.ui.viewmodel.EventViewModel

@Composable
@Preview
fun LoginScreen(event: EventViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        Column {
            var phone by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            TextField(
                value = phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                placeholder = { Text(stringResource(R.string.phone)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    phone = it
                })
            TextField(
                value = password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { Text(stringResource(R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    password = it
                })
            Button(
                modifier = Modifier.fillMaxWidth(),
                elevation = null,
                onClick = {
                    event.login(mobilePhone = phone, password = password)
                }) {
                Text(stringResource(R.string.login))
            }
        }
    }
}