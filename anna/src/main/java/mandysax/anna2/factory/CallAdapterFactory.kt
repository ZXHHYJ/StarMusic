package mandysax.anna2.factory

import mandysax.anna2.Anna2
import java.lang.reflect.Method

class CallAdapterFactory {

    private val callAdapterSet = HashSet<Factory<*>>()

    fun add(factory: Factory<*>) {
        callAdapterSet.add(factory)
    }

    fun <T> create(
        clazz: Class<T>,
        anna2: Anna2,
        method: Method,
        args: Array<Any?>?
    ): T {
        @Suppress("UNCHECKED_CAST")
        try {
            for (f in callAdapterSet)
                if (f.parsingType(clazz)) {
                    return f.create(anna2, method, args) as T
                }
            return DefaultCallAdapterFactory().create(anna2, method, args) as T
        } catch (e: IllegalStateException) {
            throw IllegalStateException("CallAdapterFactory cannot create an operation object for ${clazz.javaClass}")
        }
    }

    interface Factory<T : Any> {
        /**
         * 用于构建操作对象
         *
         * @param anna2 Anna
         * @param method 被调用的method
         * @param args 所有参数
         * @return 操作对象
         */
        fun create(
            anna2: Anna2,
            method: Method,
            args: Array<Any?>?
        ): T

        /**
         * 判断此adapter是否可以解析此class
         *
         * @param clazz 待解析的class
         * @return 是否为adapter可解析的返回类型
         */
        fun parsingType(clazz: Class<*>): Boolean
    }
}