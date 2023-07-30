package com.zxhhyj.music.logic.utils

import com.zxhhyj.music.logic.bean.SongBean

val SongBean.Album.coverUrl
    get() = "content://media/external/audio/albumart/${id}"