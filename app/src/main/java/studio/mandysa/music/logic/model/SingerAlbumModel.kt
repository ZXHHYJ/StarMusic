package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaAlbum

class SingerAlbumModel :
    MetaAlbum {
    @Value("picUrl")
    private lateinit var picUrl: String

    @Value("name")
    private lateinit var name: String

    @Value("id")
    private lateinit var id: String

    override fun getId(): String {
        return id
    }

    override fun getCoverUrl(): String {
        return picUrl
    }

    override fun getName(): String {
        return name
    }
}
