package mandysax.anna2.callback

/**
 * @author liuxiaoliu66
 */
interface Callback<T> {

    fun onResponse(t: T?)

    fun onFailure(code: Int)
}