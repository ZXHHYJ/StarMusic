package com.zxhhyj.music.ui.common.stateprompt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme

@Composable
fun EmptyContentMessage(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = randomEmoji(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColorScheme.current.text,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = vertical)
        )
    }
}

private fun randomEmoji(): String {
    val emojis = listOf(
        "( ´•︵•` )",
        "¯\\_(ツ)_/¯",
        "(╯°□°）╯︵ ┻━┻",
        "(-_-)ゞ゛",
        "(>_<)",
        "(；′⌒`)",
        "(　´_ﾉ`)",
        "┬─┬ノ( º _ ºノ)",
        "(-ω-、)",
        "╮(￣▽￣)╭",
        "(´･_･`)",
        "(ﾉ･_-)☆",
        "( ˘･з･)",
        "（；へ：）",
        "(´-ω-`)",
        "(´・ω・`)",
        "(◞‸◟；)",
        "(⊙_⊙;)",
        "(ノ_<。)",
        "（πーπ）",
        "(>_<。)"
    )
    val index = (emojis.indices).random()
    return emojis[index]
}