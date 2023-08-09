@file:Suppress("UNUSED")

package com.zxhhyj.ui.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip

@SuppressLint("UnnecessaryComposedModifier")
internal fun Modifier.roundClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val clipModifier = Modifier.clip(RoundedCornerShape(50))
    val clickableModifier = Modifier.clickable(enabled) { onClick() }
    this
        .then(clipModifier)
        .then(clickableModifier)
}
