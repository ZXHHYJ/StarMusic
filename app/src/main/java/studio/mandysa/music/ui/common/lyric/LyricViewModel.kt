package studio.mandysa.music.ui.common.lyric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class LyricViewModel : ViewModel() {

    private val mPositionLiveData = MutableLiveData(0)

    val positionLiveData: LiveData<Int> = mPositionLiveData

    private val mLyricsLiveData = MutableLiveData<List<Pair<String, Int>>>()

    val lyricLiveData: LiveData<List<Pair<String, Int>>> = mLyricsLiveData

    var liveTime: Int
        get() = throw Exception()
        set(value) {
            mLyricsLiveData.value?.forEachIndexed { index, lrcContent ->
                if (value >= lrcContent.second) {
                    mPositionLiveData.value = index
                }
            }
        }

    var lyric: String
        get() = throw Exception()
        set(value) {
            val list = ArrayList<Pair<String, Int>>()
            for (s in value.split("\n")) {
                val data = s.replace("[", "").split("]")
                for (i in 0 until data.size - 1) {
                    try {
                        if (data[data.size - 1].trim().isEmpty())
                            continue
                        list.add(Pair(data[data.size - 1].trim(), timeStr(data[i])))
                    } catch (e: Exception) {
                    }
                }
            }
            mLyricsLiveData.value = list
            mPositionLiveData.value = 0
        }

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
        return (minute.minutes + second.seconds + millisecond.milliseconds).toInt(DurationUnit.MILLISECONDS)
    }

}