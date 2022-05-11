package studio.mandysa.music.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val Blue10 = Color(0xFF000965)
val Blue20 = Color(0xFF00159E)
val Blue30 = Color(0xFF0023DA)
val Blue40 = Color(0xFF1E40FF)
val Blue80 = Color(0xFFBBC3FF)
val Blue90 = Color(0xFFDDE0FF)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
)

@Composable
fun MandySaMusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamic: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    content: @Composable () -> Unit
) {
    val colorScheme = if (dynamic) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkColorScheme else LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}