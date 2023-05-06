package com.zxhhyj.music.service.playmanager.ktx

import com.zxhhyj.music.service.playmanager.bean.SongBean

val SongBean.Album.coverUrl
    get() = "content://media/external/audio/albumart/${id}"