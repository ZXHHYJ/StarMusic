package studio.mandysa.music.logic.ktx

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * 复制文本
 */
fun copy(context: Context, content: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val mClipData = ClipData.newPlainText("Label", content)
    cm?.setPrimaryClip(mClipData)
}