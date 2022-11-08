package studio.mandysa.music.logic.bean

import mandysax.anna2.annotation.Value
import studio.mandysa.music.service.playmanager.bean.MetaArtist

/**
 *
 * @author 黄浩
 */
class SingerBean(@Value("id") private val id: String, @Value("name") private val name: String) : MetaArtist {
    /*
    "ar": [{
			"id": 10557,
			"name": "张悬",
			"alia": ["Anpu"]
		}]
     */

    override fun getId(): String {
        return id
    }

    override fun getName(): String {
        return name
    }
}