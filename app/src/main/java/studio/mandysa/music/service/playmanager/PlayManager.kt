package studio.mandysa.music.service.playmanager

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import studio.mandysa.music.logic.config.MUSIC_URL
import studio.mandysa.music.service.playmanager.model.MetaAlbum
import studio.mandysa.music.service.playmanager.model.MetaArtist
import studio.mandysa.music.service.playmanager.model.MetaMusic


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


    private val mRunnable = object : Runnable {
        override fun run() {
            mProgress.value = mMediaPlayer?.currentPosition?.toInt() ?: return
            mHandler.postDelayed(this, 1000)
        }
    }

    private val mHandler = Handler(Looper.myLooper()!!)

    private lateinit var mContext: Application

    @Volatile
    @JvmStatic
    private var mMediaPlayer: ExoPlayer? = null
        get() {
            if (field == null) {
                field = createExoPlayer(mContext)
            }
            return field
        }

    @JvmStatic
    fun init(application: Application) {
        mContext = application
    }

    /**
     * 当前播放的歌曲
     */
    private val mChangeMusic = MutableLiveData<MetaMusic<*, *>>()

    /**
     * 播放列表
     */
    private val mPlayList = MutableLiveData<List<MetaMusic<*, *>>>()

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

    fun changePlayListLiveData(): LiveData<List<MetaMusic<*, *>>> {
        return mPlayList
    }

    fun playingMusicProgressLiveData(): LiveData<Int> {
        return mProgress
    }

    fun playingMusicDurationLiveData(): LiveData<Int> {
        return mDuration
    }

    fun changeMusicLiveData(): LiveData<MetaMusic<*, *>> {
        return mChangeMusic
    }

    fun isPaused(): Boolean {
        return pauseLiveData().value!!
    }

    fun pauseLiveData(): LiveData<Boolean> {
        return mPause
    }

    @Suppress("UNCHECKED_CAST")
    fun addNextPlay(model: MetaMusic<*, *>) {
        val list = mPlayList.value as ArrayList<MetaMusic<MetaArtist, MetaAlbum>>?
        list?.let {
            list.add(mIndex.value!! + 1, model as MetaMusic<MetaArtist, MetaAlbum>)
            mPlayList.value = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun play(list: List<*>, index: Int) {
        mPlayList.value = list as List<MetaMusic<MetaArtist, MetaAlbum>>
        updateIndex(index)
    }

    /**
     * 随机播放
     */
    @Suppress("UNCHECKED_CAST")
    fun shufflePlay(list: List<*>, index: Int) {
        val mutableList = (list as MutableList<MetaMusic<MetaArtist, MetaAlbum>>).toMutableList()
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
        mHandler.post(mRunnable)
        mMediaPlayer!!.play()
    }

    fun pause() {
        if (!mMediaPlayer!!.isPlaying)
            return
        mHandler.removeCallbacks(mRunnable)
        mMediaPlayer!!.pause()
    }

    private fun playMusic(metaMusic: MetaMusic<*, *>) {
        mProgress.value = 0
        mChangeMusic.value = metaMusic
        mMediaPlayer?.run {
            val url = MUSIC_URL + metaMusic.id
            setMediaItem(MediaItem.fromUri(url.toUri()))
            prepare()
        }
    }

    init {
        mIndex.observeForever { p1: Int ->
            if (mPlayList.value != null) {
                val metaMusic: MetaMusic<*, *> = mPlayList.value!![p1]
                playMusic(metaMusic)
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