package studio.mandysa.music.logic.network

import mandysax.anna2.Anna2
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi

/**
 * @author liuxiaoliu66
 *
 */
val api by lazy {
    Anna2.build().baseUrl("http://cloud-music.pl-fe.cn/").create(NeteaseCloudMusicApi::class.java)
}