package com.zxhhyj.music.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.round
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.LocalTextStyles
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayDeque

/**
 * 消息提示
 * @author markrenChina
 */
object ComposeToast {

    data class Toast(val icons: ImageVector, val message: String)

    private val queue = ArrayDeque<Toast>()
    private val currentMessage = MutableStateFlow<Toast?>(null)

    fun postCheckToast(message: String) {
        synchronized(queue) {
            val toast = Toast(Icons.Rounded.Check, message)
            queue.add(toast)
            if (currentMessage.value == null) {
                currentMessage.value = toast
            }
        }
    }

    fun postErrorToast(message: String) {
        synchronized(queue) {
            val toast = Toast(Icons.Rounded.ErrorOutline, message)
            queue.add(toast)
            if (currentMessage.value == null) {
                currentMessage.value = toast
            }
        }
    }

    @Composable
    fun ComposeToast() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val modelState = currentMessage.collectAsState()
            AnimatedVisibility(
                visible = modelState.value != null,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 300)
                )
            )
            {
                val model = remember {
                    modelState.value
                }
                Surface(
                    modifier = Modifier.size(200.dp),
                    shape = RoundedCornerShape(round),
                    color = LocalColorScheme.current.highBackground,
                    elevation = 0.dp,
                    border = BorderStroke(
                        1.dp,
                        LocalColorScheme.current.background
                    )
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
                            model?.icons?.let {
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
                            text = model?.message ?: "",
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
            LaunchedEffect(modelState.value) {
                delay(1500)
                //移除时去查询队列，取值
                val nextMessage = synchronized(queue) { queue.poll() ?: null }
                if (nextMessage == modelState.value) {
                    currentMessage.value = null
                } else {
                    currentMessage.value = nextMessage
                }
            }
        }
    }
}