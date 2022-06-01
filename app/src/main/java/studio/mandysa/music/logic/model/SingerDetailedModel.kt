package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class SingerDetailedModel {
    @Value("cover")
    @Path("artist")
    lateinit var cover: String

    @Value("name")
    @Path("artist")
    lateinit var name: String

    /*@Value("identities")
    @Path("artist")
    val identities = ""*/

    @Value("briefDesc")
    @Path("artist")
    lateinit var briefDesc: String

    /*@Value("identifyTag")
    @Path("artist")
    val identifyTag = ""*/
}