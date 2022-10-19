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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import studio.mandysa.music.ui.theme.anyBarColor

@Composable
fun Preview(
    modifier: Modifier = Modifier,
    refresh: suspend () -> Unit,
    content: @Composable () -> Unit
) {
    var loaded by rememberSaveable {
        mutableStateOf(false)
    }
    var progress by rememberSaveable {
        mutableStateOf(0f)
    }

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
                    refresh.invoke()
                    loaded = true
                    progress = 0f
                    animator.cancel()
                }
            })
        }
        BoxWithConstraints {
            content.invoke()
            //线的颜色
            val lineColor = anyBarColor
            Canvas(
                modifier = Modifier
                    .statusBarsPadding()
                    .align(Alignment.TopCenter)
                    .fillMaxSize(),
                onDraw = {
                    //线高
                    val lineHeight = 5.dp.toPx()
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