package mandysax.anna2.utils

import mandysax.anna2.Anna2

fun <T> Class<T>.create(): T {
    return Anna2.build().baseUrl("").create(this)
}