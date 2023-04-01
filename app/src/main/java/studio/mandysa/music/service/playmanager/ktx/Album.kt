package studio.mandysa.music.service.playmanager.ktx

import studio.mandysa.music.service.playmanager.bean.SongBean

/**
 * @author 黄浩
 */
val SongBean.Local.Album.coverUrl
    get() = "content://media/external/audio/albumart/${this.id}"