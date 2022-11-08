package studio.mandysa.music.service.playmanager.bean

/**
 * Create by HuangHao at 2022/10/29
 */
open class MateSongBean(
    open val album: String,
    open val albumId: Long,
    open val artist: String,
    open val artistId: Long,
    open val duration: Long,
    open val data: String,
    open val songName: String
)
