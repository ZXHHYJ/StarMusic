package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaMusic

class RecentSongModel : MetaMusic<SingerModel, AlbumModel> {
    @Value("resourceId")
    lateinit var resourceId: String

    @Value("playTime")
    lateinit var playTime: String

    @Value("resourceType")
    lateinit var resourceType: String

    @Value("id")
    @Path("data")
    private lateinit var id: String

    @Value("name")
    @Path("data")
    private lateinit var name: String

    @Value("al")
    @Path("data")
    private lateinit var album: AlbumModel

    @Value("ar")
    @Path("data")
    private lateinit var artists: List<SingerModel>

    override fun getArtist(): List<SingerModel> {
        return artists
    }

    override fun getId(): String {
        return id
    }

    override fun getCoverUrl(): String {
        return album.coverUrl
    }

    override fun getTitle(): String {
        return name
    }

    override fun getAlbum(): AlbumModel {
        return album
    }

}