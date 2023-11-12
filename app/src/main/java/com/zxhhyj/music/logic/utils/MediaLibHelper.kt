package com.zxhhyj.music.logic.utils

import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import java.util.Collections

object MediaLibHelper {

    val songs: List<SongBean>
        get() = (AndroidMediaLibRepository.songs + WebDavMediaLibRepository.songs).sortedWith(
            compareBy<SongBean> {
                when (SettingRepository.SongSort) {
                    SettingRepository.SongSortEnum.SONG_NAME.value -> it.songName
                    SettingRepository.SongSortEnum.SONG_DURATION.value -> it.duration
                    SettingRepository.SongSortEnum.SINGER_NAME.value -> it.artist.name
                    SettingRepository.SongSortEnum.DATE_MODIFIED.value -> it.dateModified
                    else -> null
                }
            }.let { comparator ->
                if (SettingRepository.Descending) {
                    Collections.reverseOrder(comparator)
                } else {
                    comparator
                }
            })

    val artists
        get() = songs.map { it.artist }
            .distinctBy { it.name }
            .map { it.copy() }

    val albums
        get() = songs.map { it.album }
            .distinctBy { it.name }
            .map { it.copy() }

    val SongBean.Album.songs: List<SongBean>
        get() = MediaLibHelper.songs.filter { it.album.name == this.name }

    val SongBean.Artist.songs: List<SongBean>
        get() = MediaLibHelper.songs.filter { it.artist.name == this.name }

}