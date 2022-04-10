package mandysax.anna2.annotation

/**
 * @author liuxiaoliu66
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Value(val value: String)