package studio.mandysa.music.ui.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * 消息提示
 * @author markrenChina
 */
@SuppressLint("LogNotTimber")
object POPWindows {
    private const val TAG = "POPWindows"

    private val queue = ConcurrentLinkedQueue<String>()
    private val msgList = MutableList(3) { MutableStateFlow("") }
    private val position = MutableStateFlow(Position.TOP_LEFT)
    private val lock = ReentrantLock()

    enum class Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    /**
     * 禁止使用log工具，会产生死锁
     */
    //@Synchronized
    fun postValue(value: String) {
        //存入一个队列
        queue.add(value)
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            return
        }
        try {
            //遍历是否有空闲显示位
            for (e in msgList) {
                if (e.value == "") {
                    e.value = queue.poll() ?: ""
                }
            }
        } finally {
            lock.unlock()
        }
    }


    fun setPosition(pos: Position) {
        Log.i(TAG, "setPosition")
        position.value = pos
    }


    @Composable
    fun PopWin() {
        val pos by position.collectAsState()
        var modifier: Modifier = Modifier
        Log.i(TAG, "PopWin: $pos")
        when (pos) {
            Position.TOP_RIGHT -> {
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        Log.i(TAG, "PopWin: ${placeable.width}")
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.placeRelative(constraints.maxWidth - placeable.width, 0)
                        }
                    }
            }
            Position.BOTTOM_LEFT -> {
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        placeable.placeRelative(0, constraints.maxHeight - placeable.height)
                    }
                }
            }
            Position.BOTTOM_RIGHT -> {
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        placeable.placeRelative(
                            constraints.maxWidth - placeable.width,
                            constraints.maxHeight - placeable.height
                        )
                    }
                }
            }
            else -> {
            }
        }
        Wins(modifier = modifier.systemBarsPadding(), pos)
    }

    @Composable
    private fun Wins(
        modifier: Modifier,
        pos: Position
    ) {
        Column(modifier = modifier) {
            repeat(msgList.size) {
                PopItem(index = it, pos)
            }
        }
    }

    @Composable
    private fun PopItem(index: Int, pos: Position) {

        val msg by msgList[index].collectAsState()

        AnimatedVisibility(
            visible = (msg.trim() != ""),
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth ->
                    when (pos) {
                        Position.TOP_LEFT, Position.BOTTOM_LEFT -> -fullWidth
                        else -> fullWidth
                    }
                },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth ->
                    when (pos) {
                        Position.TOP_LEFT, Position.BOTTOM_LEFT -> -fullWidth
                        else -> fullWidth
                    }
                },
                animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
            )
        )
        //if (msg != "")
        {
            BoxWithConstraints(
                modifier = Modifier
                    .padding(10.dp)
                    .width(200.dp)
                    .background(color = Color.Black.copy(0.6f))
            ) {
                Text(
                    text = msg,
                    color = when {
                        msg.contains("警告") -> Color.Yellow
                        msg.contains("错误") -> Color.Yellow
                        else -> Color.White
                    },
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    fontSize = 15.sp
                )
            }
        }
        LaunchedEffect(key1 = msg) {
            if (msg != "") {
                delay(3000)
                //移除时去查询队列，取值
                val new = queue.poll() ?: ""
                if (msg == new) {
                    msgList[index].value = ""
                } else {
                    msgList[index].value = new
                }
            }
        }
    }

}