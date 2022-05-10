package studio.mandysa.music.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import studio.mandysa.music.R

@Composable
@Preview
fun LoginScreen() {
    Column(modifier = Modifier.fillMaxWidth().height(400.dp)) {
        Text(text = stringResource(id = R.string.login))
        var phone by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        TextField(value = phone, onValueChange = { phone = it })
        TextField(value = password, onValueChange = { password = it })
    }
}