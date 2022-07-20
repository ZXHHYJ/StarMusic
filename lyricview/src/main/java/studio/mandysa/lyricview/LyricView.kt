package studio.mandysa.lyricview

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import studio.mandysa.jiuwo.ktx.linear
import studio.mandysa.jiuwo.ktx.recyclerAdapter
import studio.mandysa.jiuwo.ktx.setup
import studio.mandysa.lyricview.databinding.ItemLyricBinding


class LyricView : RecyclerView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private val layoutManager
        get() = getLayoutManager() as LinearLayoutManager?

    @Suppress("UNCHECKED_CAST")
    private fun init() {
        linear().setup {
            addType<LrcProcess.LrcContent>(R.layout.item_lyric) {

                ItemLyricBinding.bind(itemView).root.apply {
                    setTextColor(context.getColor(R.color.translucent_white))
                    text = model.lrc
                    setOnClickListener {
                        mChildTime.value = model.time
                    }
                    mSelectLyricIndex.observeForever {
                        if (it == modelPosition) {
                            setTextColor(context.getColor(android.R.color.white))
                        } else {
                            setTextColor(context.getColor(R.color.translucent_white))
                        }
                    }
                    val observer = Observer<Int> { t ->
                        if (t == modelPosition) {
                            setTextColor(context.getColor(android.R.color.white))
                        } else {
                            setTextColor(context.getColor(R.color.translucent_white))
                        }
                    }
                    onAttached {
                        mSelectLyricIndex.observeForever(observer)
                    }
                    onDetached {
                        mSelectLyricIndex.removeObserver(observer)
                    }
                    onRecycled {
                        mSelectLyricIndex.removeObserver(observer)
                    }
                }
            }
        }
        mSelectLyricIndex.observeForever {
            if (mLastTouchTime + 2000 < System.currentTimeMillis()) {
                var pos = it
                if (pos > 1) pos -= 1
                scrollItemToTop(pos)
            }
        }
    }

    private fun scrollItemToTop(position: Int) {
        val smoothScroller: LinearSmoothScroller = LinearTopSmoothScroller(context)
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    internal class LinearTopSmoothScroller(context: Context?) :
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

    private val mSelectLyricIndex = MutableLiveData<Int>()

    private val mChildTime = MutableLiveData<Int>()

    private var mLastTouchTime: Long = 0

    fun getChildTimeLiveData(): LiveData<Int> = mChildTime

    @Suppress("UNCHECKED_CAST")
    var time: Int = 0
        set(value) {
            val models: List<LrcProcess.LrcContent>? =
                recyclerAdapter.models as List<LrcProcess.LrcContent>?
            models?.forEachIndexed { index, lrcContent ->
                if (value >= lrcContent.time) {
                    mSelectLyricIndex.value = index
                }
            }
            field = value
        }

    var lyric: String? = null
        set(value) {
            if (value != null) {
                recyclerAdapter.models = LrcProcess(value).lrcContent
                scrollToPosition(0)
            }
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
                        if (data[data.size - 1].trim().isEmpty())
                            continue
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
}