package studio.mandysa.music.logic.bean

import studio.mandysa.music.service.playmanager.bean.Song

/**
 * @author 黄浩
 */
data class SearchSongBean(
    override val id: String,
    val name: String,
    override val coverUrl: String,
    val ar: Array<Artist>,
    val al: Album
) : Song.NetworkBean(
    id,
    name,
    al.coverUrl,
    ar,
    al
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchSongBean

        if (id != other.id) return false
        if (name != other.name) return false
        if (coverUrl != other.coverUrl) return false
        if (!ar.contentEquals(other.ar)) return false
        if (al != other.al) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + coverUrl.hashCode()
        result = 31 * result + ar.contentHashCode()
        result = 31 * result + al.hashCode()
        return result
    }

}