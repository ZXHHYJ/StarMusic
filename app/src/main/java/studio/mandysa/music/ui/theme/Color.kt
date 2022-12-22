package studio.mandysa.music.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kyant.monet.a1
import com.kyant.monet.a2
import com.kyant.monet.*

//字体颜色
val textColor: Color
    @Composable get() = 2.a1..Color.White

//字体颜色的辅助色
val textColorLight
    @Composable get() = 4.a1..Color.White

//半透明白色的文字
val translucentWhite = Color(0x80FFFFFF)

//translucentWhite没有效果时使用
val translucentWhiteFixBug = Color(0x33FFFFFF)

//背景色
val background
    @Composable get() = Color.White..Color.Black

//背景色的反色
val onBackground
    @Composable get() = MaterialTheme.colors.onBackground

//dialog背景色
val dialogBackground
    @Composable get() = 94.a2..18.a2

//空图片背景色
val emptyImageBackground
    @Composable get() = 76.a2..50.a2

//容器颜色
val containerColor
    @Composable get() = 86.a2..70.a2

//--------------------------------------------------------------------------------------------------------------

//bar颜色
val barColor
    @Composable get() = containerColor

//barItem指示颜色
val barItemColor
    @Composable get() = 90.a2..25.a2

//tab选中颜色
val tabSelectColor
    @Composable get() = 50.a2..25.a2

//tab未选中颜色
val tabUnSelectColor
    @Composable get() = Color.Black..Color.White