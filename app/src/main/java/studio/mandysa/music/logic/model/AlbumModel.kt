package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.AlbumModel as Album

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
class AlbumModel : Album {

    @Value("id")
    private val id = ""

    @Value("name")
    private val name = ""

    @Value("picUrl")
    private val picUrl = ""

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