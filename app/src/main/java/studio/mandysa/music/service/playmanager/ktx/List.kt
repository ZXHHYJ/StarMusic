package studio.mandysa.music.service.playmanager.ktx

import studio.mandysa.music.service.playmanager.model.ArtistModel

fun List<ArtistModel>.allArtist(): String {
    var string = ""
    for (i in 0 until size) {
        if (i != 0)
            string += "/"
        string += get(i).name
    }
    return string
}
