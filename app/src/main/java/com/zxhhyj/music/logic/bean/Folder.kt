package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(val path: String, val songs: List<SongBean>) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Folder -> {
                other.path == path
            }

            else -> {
                super.equals(other)
            }
        }
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}
