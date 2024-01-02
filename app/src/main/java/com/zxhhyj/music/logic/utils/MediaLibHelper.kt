package com.zxhhyj.music.logic.utils

import com.github.promeg.pinyinhelper.Pinyin
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository

object MediaLibHelper {

    private val androidMediaLibSongs
        get() = if (SettingRepository.EnableAndroidMediaLibs) {
            AndroidMediaLibRepository.songs.sortedBy { it ->
                when (SettingRepository.SongSortEnum.entries[SettingRepository.SongSort]) {
                    SettingRepository.SongSortEnum.SONG_NAME -> Pinyin.toPinyin(it.songName[0])
                    SettingRepository.SongSortEnum.SONG_DURATION -> it.duration.toString()
                    SettingRepository.SongSortEnum.SINGER_NAME -> it.artistName?.get(0)
                        ?.let { Pinyin.toPinyin(it) }

                    SettingRepository.SongSortEnum.DATE_MODIFIED -> it.dateModified.toString()
                }
            }
        } else emptyList()

    private val webdavMediaLibSongs
        get() = if (SettingRepository.EnableWebDav) {
            WebDavMediaLibRepository.songs.sortedBy {
                when (SettingRepository.SongSortEnum.entries[SettingRepository.SongSort]) {
                    SettingRepository.SongSortEnum.SONG_NAME -> Pinyin.toPinyin(
                        it.data.substringAfterLast(
                            "/"
                        )[0]
                    )

                    SettingRepository.SongSortEnum.SONG_DURATION -> null
                    SettingRepository.SongSortEnum.SINGER_NAME -> null
                    SettingRepository.SongSortEnum.DATE_MODIFIED -> null
                }
            }
        } else emptyList()

    val songs: List<SongBean>
        get() = androidMediaLibSongs + webdavMediaLibSongs

    val artists
        get() = songs.distinctBy { it.artistName }.map { it.artistName }

    val albums
        get() = songs.distinctBy { it.albumName }.map { it.albumName }

    object Album {
        operator fun get(albumName: String) =
            songs.filter { it.albumName == albumName }
    }

    object Artist {
        operator fun get(artistName: String) =
            songs.filter { it.artistName == artistName }
    }

}