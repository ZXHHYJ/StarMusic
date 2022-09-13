package studio.mandysa.music.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.monet.PaletteStyle
import sudio.mandysa.mytheme.theme.MyTheme

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    MyTheme(keyColor = Color.Blue, style = PaletteStyle.Rainbow) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            content.invoke()
        }
    }
}