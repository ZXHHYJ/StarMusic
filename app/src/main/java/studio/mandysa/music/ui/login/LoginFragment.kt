package studio.mandysa.music.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import studio.mandysa.music.R
import studio.mandysa.music.ui.viewmodel.EventViewModel

class LoginFragment : BottomSheetDialogFragment() {

    @Composable
    @Preview(showBackground = true)
    fun LoginScreen(eventViewModel: EventViewModel = viewModel()) {
        Column {
            var phone by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            TextField(
                value = phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                placeholder = { Text(getString(R.string.phone)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    phone = it
                })
            TextField(
                value = password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { Text(getString(R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    password = it
                })
            Button(
                modifier = Modifier.fillMaxWidth(),
                elevation = null,
                onClick = {
                    eventViewModel.login(mobilePhone = phone, password = password)
                }) {
                Text(getString(R.string.login))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            it.setContent {
                LoginScreen()
            }
        }
    }
}