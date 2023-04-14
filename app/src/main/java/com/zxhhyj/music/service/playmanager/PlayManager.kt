package com.zxhhyj.music.service.playmanager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.zxhhyj.music.service.playmanager.bean.SongBean
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*


@OptIn(DelicateCoroutinesApi::class)
object PlayManager {

    /**
     * 当前播放的歌曲
     */
    private val mChangeMusic = MutableLiveData<SongBean?>()

    /**
     * 播放列表
     */
    private val mPlayList = MutableLiveData<List<SongBean>?>()

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

    fun changePlayListLiveData(): LiveData<List<SongBean>?> {
        return mPlayList
    }

    fun playingMusicProgressLiveData(): LiveData<Int> {
        return mProgress
    }

    fun playingMusicDurationLiveData(): LiveData<Int> {
        return mDuration
    }

    fun changeMusicLiveData(): LiveData<SongBean?> {
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
        initMediaPlayer()
        mMediaPlayer?.seekTo(position.toLong())
    }

    fun skipToPrevious() {
        updateIndex(mIndex.value!! - 1)
    }

    fun skipToNext() {
        updateIndex(mIndex.value!! + 1)
    }

    fun play() {
        initMediaPlayer()

        mMediaPlayer?.play()
        mPositionUpdateJob = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                mProgress.value = mMediaPlayer?.currentPosition?.toInt() ?: return@launch
                delay(1000)
            }
        }
    }

    fun pause() {
        initMediaPlayer()

        mMediaPlayer?.pause()
    }

    fun clearPlayList() {
        stop()
        mChangeMusic.value = null
        mPlayList.value = null
    }

    init {
        mIndex.observeForever {
            if (mPlayList.value != null) {
                val song = mPlayList.value!![it]
                playMusic(song)
            }
        }
    }

    private lateinit var mApplication: Application

    private var mPositionUpdateJob: Job? = null

    private var mMediaPlayer: ExoPlayer? = null

    @Synchronized
    private fun createExoPlayer() = ExoPlayer.Builder(mApplication).build().apply {
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

    private fun initMediaPlayer() {
        if (mMediaPlayer != null) return

        mMediaPlayer = createExoPlayer()
        val process = mProgress.value
        mChangeMusic.value?.let {
            playMusic(it)
        }
        process?.let {
            seekTo(it)
        }
    }

    private fun playMusic(song: SongBean) {
        initMediaPlayer()

        mProgress.value = 0
        mChangeMusic.value = song
        mMediaPlayer?.let {
            when (song) {
                is SongBean.Local -> {
                    it.setMediaItem(MediaItem.fromUri(song.data))
                    it.prepare()
                }
                is SongBean.Network -> {
                    /*val url = MUSIC_URL + song.id
                    setMediaItem(MediaItem.fromUri(url))
                    prepare()*/
                }
            }
        }
    }

    private fun updateIndex(index: Int) {
        if (!(index >= 0 && mPlayList.value != null && index <= mPlayList.value!!.size - 1)) {
            pause()
            return
        }
        mIndex.value = index
    }

    fun init(application: Application) {
        mApplication = application
    }

    fun stop() {
        mPositionUpdateJob?.cancel()
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

}