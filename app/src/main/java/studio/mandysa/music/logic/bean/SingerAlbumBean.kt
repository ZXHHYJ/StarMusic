package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.bean.MetaAlbum

class SingerAlbumBean : MetaAlbum {
    @Value("picUrl")
    private lateinit var picUrl: String

    @Value("name")
    private lateinit var name: String

    @Value("id")
    private lateinit var id: String

    @Value("publishTime")
    private lateinit var publishTime: String

    override fun getId(): String {
        return id
    }

    override fun getCoverUrl(): String {
        return picUrl
    }

    override fun getName(): String {
        return name
    }

    override fun getPublishTime(): String {
        return publishTime
    }
}
