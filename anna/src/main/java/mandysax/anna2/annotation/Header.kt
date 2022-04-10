package mandysax.anna2.annotation

/**
 * @author liuxiaoliu66
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Header(val value: String)