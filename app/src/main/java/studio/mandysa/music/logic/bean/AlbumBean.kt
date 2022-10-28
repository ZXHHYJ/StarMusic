package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaAlbum
import java.io.Serializable

/*
   "al": {
           "id": 32311,
           "name": "神的游戏",
           "picUrl": "https://p2.music.126.net/klOSGBRQhevtM6c9RXrM1A==/18808245906527670.jpg",
           "pic_str": "18808245906527670",
           "pic": 18808245906527670,
           "alia": ["Games We Play"]
       }
    */
class AlbumBean : MetaAlbum, Serializable {

    @Value("id")
    private lateinit var id: String

    @Value("name")
    private lateinit var name: String

    @Value("picUrl")
    private lateinit var picUrl: String

    override fun getId(): String {
        return id
    }

    override fun getCoverUrl(): String {
        return picUrl
    }

    override fun getName(): String {
        return name
    }

    override fun getPublishTime(): String? {
        return null
    }
}