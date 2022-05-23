package studio.mandysa.music.ui.common

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import studio.mandysa.jiuwo.ktx.linear
import studio.mandysa.jiuwo.ktx.recyclerAdapter
import studio.mandysa.jiuwo.ktx.setup
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.translucentWhite

@Composable
fun Lyric(
    modifier: Modifier = Modifier,
    lyric: String,
    liveTime: Int,
    fadingEdgeLength: Dp = 0.dp,
    onClick: (Int) -> Unit
) {
    val fadingEdgeLengthPx = LocalDensity.current.run { fadingEdgeLength.roundToPx() }
    AndroidView(modifier = modifier, factory = { it ->
        LyricView(it).apply {
            keepScreenOn = true
            isVerticalFadingEdgeEnabled = fadingEdgeLengthPx != 0
            setFadingEdgeLength(fadingEdgeLengthPx)
            getChildTimeLiveData().observeForever {
                onClick.invoke(it)
            }
        }
    }) {
        it.lyric = lyric
        it.time = liveTime
    }
}

private class LyricView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RecyclerView(
        context,
        attrs,
        defStyleAttr
    ) {

    private val layoutManager
        get() = getLayoutManager() as LinearLayoutManager?

    private fun scrollItemToTop(position: Int) {
        val smoothScroller: LinearSmoothScroller = LinearTopSmoothScroller(context)
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
        mPosition.value = position + 1
    }

    class LinearTopSmoothScroller(context: Context?) :
        LinearSmoothScroller(context) {

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 240f / displayMetrics.densityDpi
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_MOVE) {
            mLastTouchTime = System.currentTimeMillis()
        }
        return super.dispatchTouchEvent(ev)
    }

    private var mChildLcr: LrcProcess.LrcContent? = null
        @Suppress("UNCHECKED_CAST")
        set(value) {
            if (field != value) {
                val models: List<LrcProcess.LrcContent>? =
                    recyclerAdapter.models as List<LrcProcess.LrcContent>?
                if (models != null) {
                    if (mLastTouchTime + 2000 < System.currentTimeMillis()) {
                        var pos = models.indexOf(value)
                        if (pos > 1) pos -= 1
                        scrollItemToTop(pos)
                    }
                }
            }
            field = value
        }

    private val mChildTime = MutableLiveData<Int>()

    private val mPosition = MutableLiveData(0)

    private var mLastTouchTime: Long = 0

    fun getChildTimeLiveData(): LiveData<Int> = mChildTime

    @Suppress("UNCHECKED_CAST")
    var time: Int = 0
        set(value) {
            val models: List<LrcProcess.LrcContent>? =
                recyclerAdapter.models as List<LrcProcess.LrcContent>?
            if (models != null)
                for (model in models) {
                    if (value >= model.time) {
                        mChildLcr = model
                    }
                }
            field = value
        }

    var lyric: String? = null
        set(value) {
            if (value != field) {
                recyclerAdapter.models = LrcProcess(value!!).lrcContent
                scrollToPosition(0)
            }
            field = value
        }

    class LrcProcess(lyrics: String) {
        private val mList = ArrayList<LrcContent>()

        /**
         * 处理时间
         * 时间转换为毫秒millisecond
         */
        private fun timeStr(timeStr: String): Int {
            val timeData =
                timeStr
                    .replace(".", ":")
                    .split(":")
            val minute = timeData[0].toInt()
            val second = timeData[1].toInt()
            val millisecond = timeData[2].toInt()
            return (minute * 60 + second) * 1000 + millisecond * 10
        }

        val lrcContent: List<LrcContent>
            get() = mList

        /**
         * 歌词类
         * 需要排序的话，要用Integer替代int
         */
        class LrcContent(val lrc: String, val time: Int)

        /**
         * 解析歌词
         */
        init {
            for (s in lyrics.split("\n")) {
                val data = s.replace("[", "").split("]")
                for (i in 0 until data.size - 1) {
                    try {
                        mList.add(
                            LrcContent(
                                data[data.size - 1].trim(),
                                timeStr(data[i])
                            )
                        )
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    init {
        linear().setup {
            addCompose<LrcProcess.LrcContent> {
                val position by mPosition.observeAsState(0)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            mChildTime.value = model.time
                        }
                ) {
                    Text(
                        modifier = Modifier.padding(
                            vertical = 20.dp,
                            horizontal = horizontalMargin
                        ),
                        text = model.lrc,
                        color = if (position == bindingAdapterPosition) Color.White else translucentWhite,
                        fontSize = 34.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}