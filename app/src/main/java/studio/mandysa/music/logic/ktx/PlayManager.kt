package studio.mandysa.music.logic.ktx

import studio.mandysa.music.service.playmanager.PlayManager

inline fun playManager(block: PlayManager.() -> Unit) {
    block.invoke(PlayManager)
}