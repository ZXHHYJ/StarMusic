package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.MetaArtist

/**
 *
 * @author Huang hao
 */
class SingerModel : MetaArtist {

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