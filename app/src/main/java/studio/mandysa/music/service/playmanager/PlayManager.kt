package studio.mandysa.music.service.playmanager

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danikula.videocache.HttpProxyCacheServer
import studio.mandysa.music.service.playmanager.model.AlbumModel
import studio.mandysa.music.service.playmanager.model.ArtistModel
import studio.mandysa.music.service.playmanager.model.MateMusic
import java.util.*

/**
 * @author Huang hao
 */
object PlayManager {

    private lateinit var mProxy: HttpProxyCacheServer

    @JvmStatic
    fun init(context: Context) {
        if (!::mProxy.isInitialized)
            mProxy = HttpProxyCacheServer(context)
    }

    private var mLoaded = false

    /**
     * 播放器
     */
    private val mMediaPlayer = MediaPlayer()

    /**
     * 当前播放的歌曲
     */
    private val mChangeMusic = MutableLiveData<MateMusic<ArtistModel, AlbumModel>>()

    /**
     * 播放列表
     */
    private val mPlayList = MutableLiveData<List<MateMusic<ArtistModel, AlbumModel>>>()

    /**
     * 播放状态
     */
    private val mPlayState = MutableLiveData(STATE.PAUSE)

    /**
     * 一般播放状态
     */
    private val mPause = MutableLiveData(true).also { pause ->
        mPlayState.observeForever {
            when (it) {
                STATE.PLAY -> {
                    if (pause.value != false)
                        pause.value = false
                }
                STATE.LOADING, STATE.PAUSE -> {
                    if (pause.value != true)
                        pause.value = true
                }
                else -> {
                }
            }
        }
    }

    /**
     * 当前播放歌曲的下标
     */
    private val mIndex = MutableLiveData(0)

    /**
     * 当前播放歌曲进度
     */
    private val mProgress = MutableLiveData<Int>()

    /**
     * 当前播放歌曲时长
     */
    private val mDuration = MutableLiveData<Int>()

    private var mTimer: Timer? = null

    fun changePlayListLiveData(): LiveData<List<MateMusic<ArtistModel, AlbumModel>>> {
        return mPlayList
    }

    fun playingMusicProgressLiveData(): LiveData<Int> {
        return mProgress
    }

    fun playingMusicDurationLiveData(): LiveData<Int> {
        return mDuration
    }

    fun changeMusicLiveData(): LiveData<MateMusic<ArtistModel, AlbumModel>> {
        return mChangeMusic
    }

    fun playStateLivedata(): LiveData<STATE> {
        return mPlayState
    }

    fun pauseLiveData(): LiveData<Boolean> {
        return mPause
    }

    @Suppress("UNCHECKED_CAST")
    fun loadPlaylist(list: Any?, index: Int) {
        mPlayList.value = list as List<MateMusic<ArtistModel, AlbumModel>>
        updateIndex(index)
    }

    fun loadPlaylist(list: List<MateMusic<ArtistModel, AlbumModel>>, index: Int) {
        mPlayList.value = list
        updateIndex(index)
    }

    fun seekTo(position: Int) {
        mProgress.value = position
        mMediaPlayer.seekTo(position)
    }

    private fun updateIndex(index: Int) {
        if (!(index >= 0 && mPlayList.value != null && index <= mPlayList.value!!.size - 1)) {
            pause()
            return
        }
        mIndex.value = index
    }

    fun skipToPrevious() {
        updateIndex(mIndex.value!! - 1)
    }

    fun skipToNext() {
        updateIndex(mIndex.value!! + 1)
    }

    fun play() {
        if (mPlayState.value != STATE.PLAY && mLoaded) {
            if (mTimer == null) {
                mTimer = Timer().also {
                    it.schedule(object : TimerTask() {
                        override fun run() {
                            mProgress.postValue(mMediaPlayer.currentPosition)
                        }
                    }, 0, 200)
                }
            }
            mMediaPlayer.start()
            mPlayState.value = STATE.PLAY
        }
    }

    fun pause() {
        if (mPlayState.value == STATE.PLAY) {
            mTimer?.cancel()
            mTimer = null
            mMediaPlayer.pause()
            mPlayState.value = STATE.PAUSE
        }
    }

    private fun playMusic(musicModel: MateMusic<ArtistModel, AlbumModel>) {
        if (musicModel.url == mChangeMusic.value?.url) {
            if (mLoaded) {
                mMediaPlayer.seekTo(0)
            }
            return
        }
        mLoaded = false
        mPlayState.value = STATE.LOADING
        mMediaPlayer.reset()
        mDuration.value = 0
        mChangeMusic.value = musicModel
        mMediaPlayer.setOnPreparedListener {
            mLoaded = true
            mDuration.value = mMediaPlayer.duration
            play()
        }
        mMediaPlayer.setOnErrorListener { _, _, _ ->
            false
        }
        mMediaPlayer.setOnCompletionListener {
            skipToNext()
        }
        mMediaPlayer.setDataSource(
            mProxy.getProxyUrl(musicModel.url)
        )
        mMediaPlayer.prepareAsync()
    }

    init {
        mIndex.observeForever { p1: Int ->
            if (mPlayList.value != null) {
                val musicModel: MateMusic<ArtistModel, AlbumModel> = mPlayList.value!![p1]
                playMusic(musicModel)
            }
        }
    }

    fun stop() {
        mMediaPlayer.stop()
        mMediaPlayer.release()
        mTimer?.cancel()
        mTimer = null
    }

    enum class STATE {
        PLAY,
        LOADING,
        PAUSE
    }

}