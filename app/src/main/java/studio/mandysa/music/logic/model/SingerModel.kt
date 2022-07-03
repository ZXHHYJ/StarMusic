package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaArtist
import java.io.Serializable

/**
 *
 * @author 黄浩
 */
class SingerModel : MetaArtist, Serializable {

    /*
    "ar": [{
			"id": 10557,
			"name": "张悬",
			"alia": ["Anpu"]
		}]
     */

    @Value("id")
    private lateinit var id: String

    @Value("name")
    private lateinit var name: String

    override fun getId(): String {
        return id
    }

    override fun getName(): String {
        return name
    }
}