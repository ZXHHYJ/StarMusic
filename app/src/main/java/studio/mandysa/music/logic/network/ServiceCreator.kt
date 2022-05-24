package studio.mandysa.music.logic.network

import mandysax.anna2.Anna2

/**
 * @author liuxiaoliu66
 */
object ServiceCreator {
    private val ANNA = Anna2.build().baseUrl("http://cloud-music.pl-fe.cn/")
    fun <T> create(clazz: Class<T>): T {
        return ANNA.create(clazz)
    }
}