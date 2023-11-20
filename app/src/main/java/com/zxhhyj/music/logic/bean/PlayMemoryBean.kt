package com.zxhhyj.music.logic.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayMemoryBean(val index: Int? = null, val playlist: List<SongBean>? = null)
