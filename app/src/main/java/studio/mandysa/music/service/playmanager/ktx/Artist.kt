package studio.mandysa.music.service.playmanager.ktx

import studio.mandysa.music.logic.repository.LocalMediaRepository.songs
import studio.mandysa.music.service.playmanager.bean.SongBean

/**
 * @author 黄浩
 */
val SongBean.Local.Artist.coverUrl: String
    get() = this.songs[0].coverUrl
