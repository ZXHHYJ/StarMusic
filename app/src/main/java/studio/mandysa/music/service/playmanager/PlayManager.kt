package studio.mandysa.music.service.playmanager

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import studio.mandysa.music.logic.config.MUSIC_URL
import studio.mandysa.music.service.playmanager.bean.SongBean


/**
 * @author 黄浩
 */
object PlayManager {

    private fun createExoPlayer(context: Context) = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                skipToNext()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                mPause.value = !isPlaying
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


    private val mUpdateCurrentPositionFun = object : Runnable {
        override fun run() {
            mProgress.value = mMediaPlayer?.currentPosition?.toInt() ?: return
            mHandler.postDelayed(this, 1000)
        }
    }

    private val mHandler = Handler(Looper.myLooper()!!)

    private lateinit var mApplication: Application

    @Volatile
    @JvmStatic
    private var mMediaPlayer: ExoPlayer? = null
        get() {
            if (field == null) {
                field = createExoPlayer(mApplication)
            }
            return field
        }

    @JvmStatic
    fun init(application: Application) {
        mApplication = application
    }

    /**
     * 当前播放的歌曲
     */
    private val mChangeMusic = MutableLiveData<SongBean>()

    /**
     * 播放列表
     */
    private val mPlayList = MutableLiveData<List<SongBean>>()

    /**
     * 播放状态
     */
    private val mPause = MutableLiveData(true)

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

    fun changePlayListLiveData(): LiveData<List<SongBean>> {
        return mPlayList
    }

    fun playingMusicProgressLiveData(): LiveData<Int> {
        return mProgress
    }

    fun playingMusicDurationLiveData(): LiveData<Int> {
        return mDuration
    }

    fun changeMusicLiveData(): LiveData<SongBean> {
        return mChangeMusic
    }

    fun isPaused(): Boolean {
        return pauseLiveData().value!!
    }

    fun pauseLiveData(): LiveData<Boolean> {
        return mPause
    }

    @Suppress("UNCHECKED_CAST")
    fun addNextPlay(song: SongBean) {
        val list = mPlayList.value as ArrayList<SongBean>?
        list?.let {
            list.add(mIndex.value!! + 1, song)
            mPlayList.value = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun play(list: List<SongBean>, index: Int) {
        mPlayList.value = list
        updateIndex(index)
    }

    /**
     * 随机播放
     */
    fun shufflePlay(list: List<SongBean>, index: Int) {
        val mutableList = list.toMutableList()
        mutableList.shuffle()
        play(mutableList, index)
    }

    fun seekTo(position: Int) {
        mProgress.value = position
        mMediaPlayer!!.seekTo(position.toLong())
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
        if (mMediaPlayer!!.isPlaying)
            return
        mHandler.post(mUpdateCurrentPositionFun)
        mMediaPlayer!!.play()
    }

    fun pause() {
        if (!mMediaPlayer!!.isPlaying)
            return
        mHandler.removeCallbacks(mUpdateCurrentPositionFun)
        mMediaPlayer!!.pause()
    }

    private fun playMusic(song: SongBean) {
        mProgress.value = 0
        mChangeMusic.value = song
        mMediaPlayer?.run {
            when (song) {
                is SongBean.Local -> {
                    setMediaItem(MediaItem.fromUri(song.data))
                    prepare()
                }
                is SongBean.Network -> {
                    val url = MUSIC_URL + song.id
                    setMediaItem(MediaItem.fromUri(url))
                    prepare()
                }
            }
        }
    }

    init {
        mIndex.observeForever {
            if (mPlayList.value != null) {
                val song = mPlayList.value!![it]
                playMusic(song)
            }
        }
    }

    fun stop() {
        mMediaPlayer?.run {
            stop()
            release()
        }
        mMediaPlayer = null
    }

}