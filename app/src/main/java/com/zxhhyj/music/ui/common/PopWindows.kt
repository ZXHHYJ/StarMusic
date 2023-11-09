package com.zxhhyj.music.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import com.zxhhyj.ui.view.AppCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayDeque

/**
 * 消息提示
 * @author markrenChina
 */
object PopWindows {

    data class Message(val icons: ImageVector, val message: String)

    private val queue = ArrayDeque<Message>()
    private val msgList = List(3) { MutableStateFlow<Message?>(null) }


    fun postCheckMessage(message: String) {
        synchronized(queue) {
            queue.add(Message(Icons.Rounded.Check, message))
            for (e in msgList) {
                if (e.value == null) {
                    e.value = queue.poll() ?: null
                }
            }
        }
    }

    fun postErrorMessage(message: String) {
        synchronized(queue) {
            queue.add(Message(Icons.Rounded.ErrorOutline, message))
            for (e in msgList) {
                if (e.value == null) {
                    e.value = queue.poll() ?: null
                }
            }
        }
    }

    @Composable
    fun PopWin() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            repeat(msgList.size) { index ->
                val msg by msgList[index].collectAsState()
                AnimatedVisibility(
                    visible = msg != null,
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = 300)
                    )
                )
                {
                    AppCard(
                        modifier = Modifier
                            .size(200.dp),
                        backgroundColor = LocalColorScheme.current.highBackground,
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1.0f),
                                contentAlignment = Alignment.Center
                            ) {
                                msg?.icons?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = null,
                                        tint = LocalColorScheme.current.highlight,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal)
                                    )
                                }
                            }
                            Text(
                                text = msg?.message ?: "",
                                color = LocalColorScheme.current.text,
                                style = LocalTextStyles.current.main,
                                modifier = Modifier.padding(
                                    horizontal = horizontal,
                                    vertical = vertical
                                )
                            )
                        }
                    }
                }
                LaunchedEffect(msg) {
                    if (msg != null) {
                        delay(1500)
                        //移除时去查询队列，取值
                        val new = synchronized(queue) { queue.poll() ?: null }
                        if (msg == new) {
                            msgList[index].value = null
                        } else {
                            msgList[index].value = new
                        }
                    }
                }
            }
        }
    }
}