package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.model.ArtistModel

/**
 *
 * @author Huang hao
 */
class SingerModel : ArtistModel {

    /*
    "ar": [{
			"id": 10557,
			"name": "张悬",
			"alia": ["Anpu"]
		}]
     */

    @Value("id")
    private val id = ""

    @Value("name")
    private val name = ""

    override fun getId(): String {
        return id
    }

    override fun getName(): String {
        return name
    }
}