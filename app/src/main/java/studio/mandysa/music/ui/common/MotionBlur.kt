package studio.mandysa.music.ui.common

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MotionBlur(modifier: Modifier) {
    AndroidView(factory = { WebView(it) }, modifier = modifier) {
        it.settings.javaScriptEnabled = true
        it.settings.javaScriptCanOpenWindowsAutomatically = false
        it.settings.useWideViewPort = true
        it.settings.domStorageEnabled = true
        it.settings.databaseEnabled = true
        it.settings.loadWithOverviewMode = false
        it.settings.loadsImagesAutomatically = true
        it.settings.defaultTextEncodingName = "utf-8"
        it.settings.setSupportMultipleWindows(false)
        it.loadUrl("file:android_asset/motion_blur/index.html")
    }
}

@Preview
@Composable
fun PreviewMotionBlur() {
    MotionBlur(modifier = Modifier.fillMaxSize())
}