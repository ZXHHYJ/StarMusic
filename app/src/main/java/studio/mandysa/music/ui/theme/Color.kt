package studio.mandysa.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kyant.monet.a1
import com.kyant.monet.a2
import com.kyant.monet.n1
import com.kyant.monet.rangeTo

val appIconAccentColor
    @Composable get() = 45.a1

val appTextAccentColor
    @Composable get() = 45.a1

val appTextButtonAccentColor
    @Composable get() = 45.a1

val appButtonAccentColor
    @Composable get() = 50.a1

//字体颜色
val textColor: Color
    @Composable get() = 2.n1..Color.White

//字体颜色的反色
val onTextColor: Color
    @Composable get() = Color.White..Color.Black

//字体颜色的辅助色
val textColorLight
    @Composable get() = 40.n1..80.n1

//半透明白色的文字
val translucentWhite = Color(0x80FFFFFF)

//translucentWhite没有效果时使用
val translucentWhiteFixBug = Color(0x33FFFFFF)

//背景色
val appBackgroundColor
    @Composable get() = Color.White..1.n1

//背景色的反色
val onBackground
    @Composable get() = Color.Black..Color.LightGray

//空图片背景色
val emptyImageBackground
    @Composable get() = 76.a2..50.a2

//容器颜色
val cardBackgroundColor
    @Composable get() = 90.a2..70.a2

//bar颜色
val barBackgroundColor
    @Composable get() = appBackgroundColor

//barItem选中和未选中的颜色
val barContentColor
    @Composable get() = 45.a1

val appDividerColor
    @Composable get() = Color(0xd8d8d8d8)