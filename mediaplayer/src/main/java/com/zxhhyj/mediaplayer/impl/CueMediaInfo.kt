package com.zxhhyj.mediaplayer.impl

interface CueMediaInfo {
    // 歌曲时长
    val duration: Long?

    // 歌曲文件路径
    val data: String

    // 开始位置
    val startPosition: Long?

    // 结束位置
    val endPosition: Long?
}