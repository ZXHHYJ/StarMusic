package mandysax.anna2

import mandysax.anna2.factory.CallAdapterFactory
import mandysax.anna2.factory.ConverterFactory
import java.lang.reflect.Proxy

/**
 * @author liuxiaoliu66
 */
class Anna2 internal constructor() {
    var baseUrl: String? = null
        private set

    internal val converterFactory = ConverterFactory()
    private val callAdapterFactory = CallAdapterFactory()

    fun baseUrl(baseUrl: String?): Anna2 {
        this.baseUrl = baseUrl
        return this
    }

    fun addConverterFactory(factory: ConverterFactory.Factory): Anna2 {
        converterFactory.add(factory)
        return this
    }

    fun addCallAdapterFactory(factory: CallAdapterFactory.Factory<*>): Anna2 {
        callAdapterFactory.add(factory)
        return this
    }

    fun <T> create(clazz: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf<Class<*>>(clazz)
        ) { _, method, args ->
            callAdapterFactory.create(
                method.returnType,
                this,
                method,
                args
            )
        } as T
    }

    companion object {
        @JvmStatic
        val UNKNOWN = -1

        @JvmStatic
        fun build(): Anna2 {
            return Anna2()
        }
    }
}