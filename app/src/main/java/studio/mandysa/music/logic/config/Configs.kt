package studio.mandysa.music.logic.config

import studio.mandysa.fastkt.FastKt
import studio.mandysa.music.MainApplication

/**
 * @author 黄浩
 */
const val MUSIC_URL = "http://music.163.com/song/media/outer/url?id="

const val BASE_URL = "http://124.70.14.223:3000/"

//持久化FastKt
lateinit var noBackupFastKt: FastKt

//缓存FastKt
lateinit var cacheFastKt: FastKt

lateinit var mainApplication: MainApplication