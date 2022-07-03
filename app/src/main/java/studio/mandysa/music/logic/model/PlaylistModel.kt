package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import java.io.Serializable

class PlaylistModel : Serializable {
    @Value("id")
    lateinit var id: String

    @Value("name")
    lateinit var name: String

    /*@Value("copywriter")
    public String info;*/
    @Value("picUrl")
    lateinit var picUrl: String
}