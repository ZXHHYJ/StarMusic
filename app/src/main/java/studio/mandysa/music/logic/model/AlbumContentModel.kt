package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class AlbumContentModel {
    @Value("songs")
    val songList: List<MusicModel>? = null

    @Value("picUrl")
    @Path("album")
    val picUrl = ""

    @Path("album/artist")
    @Value("name")
    val artistName = ""

    @Value("company")
    @Path("album")
    val company = ""

    @Value("description")
    @Path("album")
    val description = ""

    @Value("name")
    @Path("album")
    val name = ""

    @Value("id")
    @Path("album")
    val id = ""

    @Value("publishTime")
    @Path("album")
    val publishTime = ""
}