package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.logic.network.Url
import studio.mandysa.music.service.playmanager.model.ArtistModel
import studio.mandysa.music.service.playmanager.model.MateMusic

class PersonalFmModel : MateMusic<ArtistModel, AlbumModel> {
    @Value("name")
    private val name = ""

    @Value("id")
    private val id = ""

    @Value("artists")
    private val artistsList: List<SingerModel>? = null

    override fun getArtist(): List<ArtistModel> {
        return artistsList!!
    }

    override fun getCoverUrl(): String {
        return ""
    }

    override fun getId(): String {
        return id
    }

    override fun getTitle(): String {
        return name
    }

    override fun getUrl(): String {
        return Url.MUSIC_URL + id
    }

    override fun toString(): String {
        return id
    }

    override fun getAlbum(): AlbumModel {
        TODO("Not yet implemented")
    }
}