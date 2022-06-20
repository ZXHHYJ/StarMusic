package studio.mandysa.music.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.kyant.color.palette.Hct
import com.kyant.color.palette.Hct.Companion.toHct
import com.kyant.color.palette.HctType
import com.kyant.color.palette.TonalPalettes.Companion.generateTonalPalettes
import com.kyant.color.palette.TonalPalettes.Companion.transform
import com.kyant.color.rgb.Srgb

val Blue10 = Color(0xFF000965)
val Blue20 = Color(0xFF00159E)
val Blue30 = Color(0xFF0023DA)
val Blue40 = Color(0xFF1E40FF)
val Blue80 = Color(0xFFBBC3FF)
val Blue90 = Color(0xFFDDE0FF)

//字体颜色
val textColor: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground

//灰色
val textColorLight = Color(0xFF7A7A7A)

//半透明白色
val translucentWhite = Color(0x80FFFFFF)

val onBackground: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground

//dialog背景色
val dialogBackground: Color
    @Composable get() = 94.accent2 withNight 10.accent2

//内容颜色
val contentColor: Color
    @Composable get() = 90.accent2 withNight 20.accent2

//bottomBarItem背景颜色
val neutralColor: Color
    @Composable get() = 82.accent1 withNight 15.accent2

//bottomBarItem指示颜色
val indicatorColor: Color
    @Composable get() = 90.accent2 withNight 20.accent2

fun Hct.toColor(): Color {
    val srgb = toSrgb().clamp()
    return Color(srgb.r.toFloat(), srgb.g.toFloat(), srgb.b.toFloat())
}

fun Color.toHct(): Hct {
    return Srgb(red.toDouble(), green.toDouble(), blue.toDouble()).toHct(type = HctType.Cam16)
}

val LocalTonalPalettes = staticCompositionLocalOf {
    Color.Blue.toHct().generateTonalPalettes()
}

inline val Number.accent1
    @Composable get() = with(LocalTonalPalettes.current) {
        keyColor.transform(toDouble(), style.accent1Spec).toColor()
    }

inline val Number.accent2
    @Composable get() = with(LocalTonalPalettes.current) {
        keyColor.transform(toDouble(), style.accent2Spec).toColor()
    }

@Composable
infix fun Color.withNight(nightColor: Color): Color {
    return if (isSystemInDarkTheme()) nightColor else this
}