package mandysax.anna2.factory

/**
 * @author liuxiaoliu66
 */
class ConverterFactory {

    private var mFactorySet: HashSet<Factory>? = null

    fun add(factory: Factory) {
        if (mFactorySet == null) {
            mFactorySet = HashSet()
        }
        mFactorySet!!.add(factory)
    }

    fun <T : Any> create(clazz: Class<T>, json: String?): T? {
        try {
            mFactorySet?.let {
                for (f in it) {
                    if (f.parsingType(clazz))
                        return f.create(clazz, json)
                }
            }
            return DefaultConverterFactory.create().create(clazz, json)
        } catch (e: IllegalStateException) {
            throw IllegalStateException("ConverterFactory cannot create data object for ${clazz.javaClass}")
        }
    }

    interface Factory {
        /**
         * 用于构建数据类
         *
         * @param clazz 数据类的class
         * @param json json信息
         * @return 数据类
         */
        fun <T : Any> create(clazz: Class<T>, json: String?): T?

        /**
         * 判断此factory是否可以解析此class
         *
         * @param clazz 待解析的class
         * @return 是否为factory可解析的数据类型
         */
        fun parsingType(clazz: Class<*>): Boolean
    }
}