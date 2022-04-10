package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

class SingerDetailedModel {
    @Value("cover")
    @Path("artist")
    val cover = ""

    @Value("name")
    @Path("artist")
    val name = ""

    /*@Value("identities")
    @Path("artist")
    val identities = ""*/

    @Value("briefDesc")
    @Path("artist")
    val briefDesc = ""

    /*@Value("identifyTag")
    @Path("artist")
    val identifyTag = ""*/
}