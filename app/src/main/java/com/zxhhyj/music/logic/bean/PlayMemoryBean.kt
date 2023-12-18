package com.zxhhyj.music.logic.bean

import java.io.Serializable

data class PlayMemoryBean(
    val index: Int? = null,
    val songInSongsIndexList: List<Int>?
) : Serializable
