package com.zxhhyj.music.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.theme.MandySaMusicTheme
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * 消息提示
 * @author markrenChina
 */

@Composable
@Preview
fun PreviewPopWin() {
    MandySaMusicTheme {
        POPWindows.PopWin()
        POPWindows.postValue("test")
        POPWindows.postValue("test2")
    }
}

object POPWindows {

    private val queue = ConcurrentLinkedQueue<String>()
    private val msgList = MutableList(3) { MutableStateFlow("") }
    private val lock = ReentrantLock()

    /**
     * 禁止使用log工具，会产生死锁
     */
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

    @Composable
    fun PopWin() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            repeat(msgList.size) { index ->
                val msg by msgList[index].collectAsState()
                AnimatedVisibility(
                    visible = (msg.trim().isNotEmpty()),
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth ->
                            fullWidth
                        },
                        animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth ->
                            fullWidth
                        },
                        animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
                    )
                )
                {
                    AppCard(
                        modifier = Modifier
                            .padding(10.dp)
                            .width(200.dp),
                        backgroundColor = LocalColorScheme.current.highBackground,
                        elevation = 1.dp
                    ) {
                        Text(
                            text = msg,
                            color = LocalColorScheme.current.text,
                            modifier = Modifier.padding(
                                horizontal = horizontal,
                                vertical = vertical
                            ),
                            fontSize = 16.sp
                        )
                    }
                }
                LaunchedEffect(msg) {
                    if (msg.isNotEmpty()) {
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
    }

}