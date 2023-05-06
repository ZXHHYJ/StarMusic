package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.zxhhyj.music.service.playmanager.bean.SongBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayListModel(val name: String, val songs: List<SongBean>) : Parcelable