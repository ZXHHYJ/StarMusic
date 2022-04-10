package studio.mandysa.music.logic.ktx

import studio.mandysa.music.service.playmanager.PlayManager

fun playManager(block: PlayManager.() -> Unit) {
    block.invoke(PlayManager)
}