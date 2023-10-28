package com.zxhhyj.music.logic.repository

import com.zxhhyj.music.logic.bean.SongBean

object MediaLibRepository {

    private val songs: List<SongBean>
        get() = AndroidMediaLibRepository.songs + WebDavMediaLibRepository.songs

    val artists
        get() = songs.map { it.artist }
            .distinctBy { it.name }
            .map { it.copy() }

    val albums
        get() = songs.map { it.album }
            .distinctBy { it.name }
            .map { it.copy() }

    val SongBean.Album.songs: List<SongBean>
        get() = this@MediaLibRepository.songs.filter { it.album.name == this.name }

    val SongBean.Artist.songs: List<SongBean>
        get() = this@MediaLibRepository.songs.filter { it.artist.name == this.name }

}