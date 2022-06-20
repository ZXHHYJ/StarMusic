package studio.mandysa.music.logic.ktx

import kotlin.math.abs

fun <T : Any> List<T>.getPagInfo(currentPage: Int, pageSize: Int): PagInfo<T> {
    val list = arrayListOf<T>()
    val difference = pageSize * currentPage
    for (i in difference until if (this.size - difference < pageSize)
        difference + abs(this.size - difference)
    else
        pageSize * (currentPage + 1)) {
        list.add(this[i])
    }
    return PagInfo(
        data = list,
        prevKey = if (currentPage == 0) null else currentPage - 1,
        nextKey = if (difference > this.size) null else currentPage + 1
    )
}

data class PagInfo<T>(val data: List<T>, val prevKey: Int?, val nextKey: Int?)