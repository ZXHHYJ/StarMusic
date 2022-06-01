package studio.mandysa.music.logic.network

import mandysax.anna2.Anna2
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi

/**
 * @author liuxiaoliu66
 *
 */
private val ANNA = Anna2.build().baseUrl("http://cloud-music.pl-fe.cn/")

val api = ANNA.create(NeteaseCloudMusicApi::class.java)