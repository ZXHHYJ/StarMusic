package studio.mandysa.music.service.playmanager

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import studio.mandysa.music.service.playmanager.model.AlbumModel
import studio.mandysa.music.service.playmanager.model.ArtistModel
import studio.mandysa.music.service.playmanager.model.MateMusic


/**
 * @author Huang hao
 */
object PlayManager {

    private val mPlayer by lazy {
        ExoPlayer.Builder(mContext, object : DefaultRenderersFactory(mContext) {}).build().apply {
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    skipToNext()
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_READY -> {
                            mDuration.value = this@apply.duration.toInt()
                            this@PlayManager.play()
                        }
                        Player.STATE_BUFFERING -> {

                        }
                        Player.STATE_ENDED -> {
                            skipToNext()
                        }
                        Player.STATE_IDLE -> {

                        }
                        else -> {}
                    }
                }
            })
        }
    }

    private val mRunnable = object : Runnable {
        override fun run() {
            mProgress.value = mPlayer.currentPosition.toInt()
            mHandler.postDelayed(this, 200)
        }
    }

    private val mHandler = Handler(Looper.myLooper()!!)

    private lateinit var mContext: Application

    @JvmStatic
    fun init(application: Application) {
        mContext = application
    }

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
        mPlayer.seekTo(position.toLong())
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
        if (mPlayState.value != STATE.PLAY) {
            mHandler.post(mRunnable)
            mPlayer.play()
            mPlayState.value = STATE.PLAY
        }
    }

    fun pause() {
        if (mPlayState.value == STATE.PLAY) {
            mHandler.removeCallbacks(mRunnable);
            mPlayer.pause()
            mPlayState.value = STATE.PAUSE
        }
    }

    private fun playMusic(musicModel: MateMusic<ArtistModel, AlbumModel>) {
        if (musicModel.url == mChangeMusic.value?.url) {
            mPlayer.seekTo(0)
            return
        }
        mPlayState.value = STATE.LOADING
        mDuration.value = 0
        mChangeMusic.value = musicModel
        mPlayer.setMediaItem(MediaItem.fromUri(musicModel.url.toUri()))
        mPlayer.prepare()
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
        mPlayer.stop()
        mPlayer.release()
    }

    enum class STATE {
        PLAY,
        LOADING,
        PAUSE
    }

}