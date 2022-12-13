package studio.mandysa.music.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MotionBlur(modifier: Modifier) {
    val state = rememberWebViewState("file:android_asset/motion_blur/index.html")
    WebView(
        state = state,
        onCreated = {
            it.settings.javaScriptEnabled = true
            it.settings.domStorageEnabled = true
        },
        captureBackPresses = false,
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewMotionBlur() {
    MotionBlur(modifier = Modifier.fillMaxSize())
}