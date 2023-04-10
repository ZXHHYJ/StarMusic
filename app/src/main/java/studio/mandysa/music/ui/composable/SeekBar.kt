package studio.mandysa.music.ui.composable

import android.widget.SeekBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView



@Composable
fun SeekBar(modifier: Modifier, value: Int, maxValue: Int, onValueChange: (Int) -> Unit) {
    AndroidView(modifier = modifier, factory = {
        SeekBar(it).apply {
            thumb = null
            background = null
            setPadding(0, 0, 0, 0)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) onValueChange.invoke(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
        }
    }) {
        it.progress = value
        it.max = maxValue
    }
}