package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

class ListModel {
    @Value("list")
    val list: List<Model>? = null

    class Model {
        @Value("coverImgUrl")
        val coverImgUrl = ""

        @Value("updateFrequency")
        val updateFrequency = ""

        @Value("description")
        val description = ""

        @Value("name")
        val name = ""

        @Value("id")
        val id = ""
    }
}