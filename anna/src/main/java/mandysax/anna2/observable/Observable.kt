package mandysax.anna2.observable

import mandysax.anna2.callback.Callback

/**
 * @author liuxiaoliu66
 */
interface Observable<T> {

    /**
     * Set
     *
     * @param callback Callback回调
     */
    fun set(callback: Callback<T>)

    /**
     * Cancel
     *
     * 取消当前请求
     */
    fun cancel()

    /**
     * Clone
     *
     * @return 各项参数相同的新对象
     */
    fun clone(): Observable<T>
}