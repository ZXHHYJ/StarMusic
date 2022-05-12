package studio.mandysa.music.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
@Preview
fun LoginDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(100.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}