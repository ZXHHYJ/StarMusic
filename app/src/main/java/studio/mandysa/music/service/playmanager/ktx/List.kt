package studio.mandysa.music.service.playmanager.ktx

import studio.mandysa.music.service.playmanager.bean.MetaArtist

fun List<MetaArtist>.allArtist(): String {
    val builder = StringBuilder()
    for (i in indices) {
        if (i != 0) {
            builder.append("/")
        }
        builder.append(get(i).name)
    }
    return builder.toString()
}
