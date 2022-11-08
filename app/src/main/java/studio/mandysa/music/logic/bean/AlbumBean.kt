package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.bean.MetaAlbum

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
class AlbumBean(
    @Value("id") private val id: String,
    @Value("name") private val name: String,
    @Value("picUrl") private val picUrl: String
) : MetaAlbum {

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