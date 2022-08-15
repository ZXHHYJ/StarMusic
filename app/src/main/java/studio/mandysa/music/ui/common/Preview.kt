package studio.mandysa.music.ui.common

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.launch
import studio.mandysa.music.ui.theme.neutralColor

@Composable
fun Preview(
    modifier: Modifier = Modifier,
    load: suspend () -> Unit,
    content: @Composable () -> Unit
) {
    var loaded by rememberSaveable {
        mutableStateOf(false)
    }
    var progress by remember {
        mutableStateOf(0f)
    }
    val lineColor = neutralColor
    val lineHeight = 10f

    val animator = ValueAnimator.ofFloat(0f, 1f)

    Box(modifier = modifier) {
        //网络请求
        if (!loaded) {
            DisposableEffect(key1 = this, effect = {
                //属性动画初始化
                animator.duration = 600
                animator.interpolator = LinearInterpolator()
                animator.addUpdateListener {
                    progress = it.animatedValue as Float
                    if (loaded && progress == 1f) {
                        animator.repeatCount = 1
                    }
                }
                animator.repeatCount = ValueAnimator.INFINITE
                animator.start()
                //避免内存泄漏
                onDispose {
                    animator.cancel()
                }
            })
            LaunchedEffect(key1 = this, block = {
                launch {
                    load.invoke()
                    loaded = true
                }
            })
        }
        BoxWithConstraints {
            content.invoke()
            Canvas(
                modifier = Modifier
                    .statusBarsPadding()
                    .align(Alignment.TopCenter)
                    .fillMaxSize(),
                onDraw = {
                    val width = maxWidth.toPx()
                    val progressWidth = width / 2 * progress
                    drawLine(
                        color = lineColor,
                        strokeWidth = lineHeight * (1f - progress),
                        start = Offset(width / 2 - progressWidth, 0f),
                        end = Offset(width / 2 + progressWidth, 0f),
                        alpha = 1f - progress
                    )
                })
        }
    }
}