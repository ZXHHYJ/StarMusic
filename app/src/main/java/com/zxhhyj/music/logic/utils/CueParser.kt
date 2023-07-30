package com.zxhhyj.music.logic.utils

object CueParser {

    /**
     * CUE 文件数据类
     */
    data class CueData(
        val performer: String?, // 表演者
        val title: String?, // 标题
        val tracks: List<CueTrackData> // 轨道列表
    )

    /**
     * CUE 文件轨道数据类
     */
    data class CueTrackData(
        val title: String, // 标题
        val performer: String, // 表演者
        val startPosition: String, // 开始位置
        val endPosition: String // 结束位置
    )

    fun parseCueContent(cueContent: String): CueData {
        val performerRegex = Regex("""PERFORMER "(.+)"""")
        val titleRegex = Regex("""TITLE "(.+)"""")
        val trackRegex =
            Regex("""TRACK (\d+) AUDIO\n.*TITLE "(.*)"\n.*PERFORMER "(.*)"\n.*INDEX (\d\d) (\d\d:\d\d:\d\d).*""")
        val performer = performerRegex.find(cueContent)?.groupValues?.get(1)
        val title = titleRegex.find(cueContent)?.groupValues?.get(1)
        val tracks = trackRegex.findAll(cueContent).mapIndexed { _, matchResult ->
            val values = matchResult.groupValues
            CueTrackData(
                values[2],
                values[3],
                values[5],
                trackRegex.findNext(matchResult)?.groupValues?.get(5) ?: "00:00:00"
            )
        }
        return CueData(performer, title, tracks.toList())
    }

    private fun Regex.findNext(matchResult: MatchResult): MatchResult? {
        return matchResult.next()?.let { this.find(it.value) }
    }
}