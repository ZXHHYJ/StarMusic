package studio.mandysa.music.logic.config

import mandysax.anna2.Anna2
import mandysax.anna2.factory.DataClassConverterFactory
import studio.mandysa.music.logic.bean.NeteaseCloudMusicApi

/**
 * @author 黄浩
 *
 */
val api = Anna2
    .build()
    .baseUrl(BASE_URL)
    .addConverterFactory(DataClassConverterFactory())
    .create(NeteaseCloudMusicApi::class.java)