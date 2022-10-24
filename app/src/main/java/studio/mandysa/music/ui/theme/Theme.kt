package studio.mandysa.music.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes
import com.kyant.monet.dynamicColorScheme

@Composable
fun MandySaMusicTheme(
    content: @Composable () -> Unit
) {
    // Obtain a key color
    val color = Color.White
    // Generate tonal palettes with TonalSpot (default) style
    val palettes = color.toTonalPalettes(style = PaletteStyle.Content)
    // In your Theme.kt
    CompositionLocalProvider(LocalTonalPalettes provides palettes) {
        // Map TonalPalettes to Compose Material3 ColorScheme
        val colorScheme = dynamicColorScheme()
        MaterialTheme(colorScheme = colorScheme) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
            ) {
                content.invoke()
            }
        }
    }
}