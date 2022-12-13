package studio.mandysa.music.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MotionBlur(modifier: Modifier, url: String) {
    val context = LocalContext.current
    var color1 by remember {
        mutableStateOf<String?>(null)
    }
    var color2 by remember {
        mutableStateOf<String?>(null)
    }
    var color3 by remember {
        mutableStateOf<String?>(null)
    }
    var color4 by remember {
        mutableStateOf<String?>(null)
    }
    var color5 by remember {
        mutableStateOf<String?>(null)
    }
    var color6 by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(key1 = url) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val bitmap = imageLoader.execute(request).drawable?.toBitmap()
        bitmap?.let { it ->
            Palette.from(it).generate {
                color1 = "${it?.darkMutedSwatch?.rgb}"
                color2 = "${it?.darkVibrantSwatch?.rgb}"
                color3 = "${it?.lightVibrantSwatch?.rgb}"
                color4 = "${it?.lightMutedSwatch?.rgb}"
                color5 = "${it?.mutedSwatch?.rgb}"
                color6 = "${it?.vibrantSwatch?.rgb}"
            }
        }
    }
    WebView(
        state = rememberWebViewState("file:android_asset/motion_blur/index.html?color1=$color1&color2=$color2&color3=$color3&color4=$color4&color5=$color5&color6=$color6"),
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
    MotionBlur(modifier = Modifier.fillMaxSize(), "")
}