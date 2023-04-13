package com.zxhhyj.music.service.playmanager.ktx

import com.zxhhyj.music.logic.repository.LocalMediaRepository.songs
import com.zxhhyj.music.service.playmanager.bean.SongBean


val SongBean.Local.Artist.coverUrl: String
    get() = this.songs[0].coverUrl
