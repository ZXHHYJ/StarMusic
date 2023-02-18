package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@Composable
fun BoxWithPercentages(modifier: Modifier, content: @Composable BoxWithPercentagesScope.() -> Unit) {
    BoxWithConstraints(modifier = modifier) {
        content.invoke(BoxWithPercentagesScope(constraints, maxHeight, maxWidth, minHeight, minWidth))
    }
}

class BoxWithPercentagesScope(
    val constraints: Constraints,
    val maxHeight: Dp,
    val maxWidth: Dp,
    val minHeight: Dp,
    val minWidth: Dp
) {

    val Int.wp: Dp
        get() = maxWidth * this / 100

    val Int.hp: Dp
        get() = maxHeight * this / 100

}

