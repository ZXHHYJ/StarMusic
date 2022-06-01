package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

class SingerAlbumModel {

    @Value("id")
    lateinit var id: String

    @Value("name")
    lateinit var name: String

    @Value("artist")
    lateinit var album: AlbumModel
}