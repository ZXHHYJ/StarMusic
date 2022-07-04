package studio.mandysa.music.logic.network

import mandysax.anna2.Anna2
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi

/**
 * @author 黄浩
 *
 */
val api by lazy {
    Anna2
        .build()
        .baseUrl(BASE_URL)
        .create(NeteaseCloudMusicApi::class.java)
}