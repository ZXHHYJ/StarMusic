package studio.mandysa.music.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable

//默认圆角形状，除非使用这个不协调，否则不要使用其他shape
val cornerShape: RoundedCornerShape
    @Composable get() = RoundedCornerShape(round)