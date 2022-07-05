package studio.mandysa.music.logic.repository

import androidx.lifecycle.LiveData
import studio.mandysa.fastkt.serialLiveData
import studio.mandysa.music.logic.config.noBackup
import studio.mandysa.music.logic.model.SerialSongModel
import studio.mandysa.music.service.playmanager.PlayManager

object PlayHistoryRepository {
    private val mPlayHistoryLiveData by serialLiveData<ArrayList<SerialSongModel>>(
        key = "play_history",
        def = arrayListOf(),
        fastKt = noBackup
    )

    val playHistoryLiveData: LiveData<ArrayList<SerialSongModel>> = mPlayHistoryLiveData

    init {
        PlayManager.changeMusicLiveData().observeForever { it ->
            val serialSongModel = SerialSongModel(
                title = it.title,
                id = it.id,
                coverUrl = it.coverUrl,
                artists = arrayListOf<SerialSongModel.SerialArtist>().apply {
                    it.artist.forEach {
                        this.add(SerialSongModel.SerialArtist(id = it.id, it.name))
                    }
                },
                album = SerialSongModel.SerialAlbum(
                    id = it.album.id,
                    coverUrl = it.album.coverUrl,
                    name = it.album.name,
                    publishTime = it.album.name
                )
            )
            mPlayHistoryLiveData.apply {
                val mutableList = value!!.toMutableList()
                if (mutableList.size >= 10) {
                    mutableList.removeAt(0)
                }
                for (i in 0 until mutableList.size) {
                    val model = mutableList[i]
                    if (model.id == serialSongModel.id) {
                        mutableList.removeAt(i)
                        break
                    }
                }
                mutableList.add(0, serialSongModel)
                value = mutableList.toMutableList() as ArrayList
            }
        }
    }
}