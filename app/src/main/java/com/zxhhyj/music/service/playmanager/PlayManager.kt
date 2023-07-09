package com.zxhhyj.music.service.playmanager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.zxhhyj.music.service.playmanager.bean.SongBean
import com.zxhhyj.music.service.playmanager.impl.PlayManagerController
import com.zxhhyj.music.service.playmanager.impl.PlayManagerState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
object PlayManager : PlayManagerState, PlayManagerController, Player.Listener {

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

    override fun changePlayListLiveData(): LiveData<List<SongBean>?> {
        return mPlayList
    }

    override fun progressLiveData(): LiveData<Int> {
        return mProgress
    }

    override fun durationLiveData(): LiveData<Int> {
        return mDuration
    }

    override fun changeMusicLiveData(): LiveData<SongBean?> {
        return mChangeMusic
    }

    override val isPaused: Boolean
        get() = pauseLiveData().value ?: true

    override fun pauseLiveData(): LiveData<Boolean> {
        return mPause
    }

    override fun play(list: List<SongBean>, index: Int) {
        mPlayList.value = list
        updateIndex(index)
    }

    override fun seekTo(position: Int) {
        mProgress.value = position
        initMediaPlayer()
        mMediaPlayer?.seekTo(position.toLong())
    }

    override fun skipToPrevious() {
        updateIndex(mIndex.value!! - 1)
    }

    override fun skipToNext() {
        updateIndex(mIndex.value!! + 1)
    }

    override fun play() {
        initMediaPlayer()
        mMediaPlayer?.play()
    }

    override fun pause() {
        initMediaPlayer()
        mMediaPlayer?.pause()
    }

    override fun addNextPlay(song: SongBean) {
        mPlayList.value?.let {
            mPlayList.value = it.toMutableList().apply {
                add((mIndex.value ?: return) + 1, song)
            }
        }
    }

    override fun deleteSong(song: SongBean) {

    }

    override fun clearPlayList() {
        stop()
        mChangeMusic.value = null
        mPlayList.value = null
    }

    init {
        mIndex.observeForever {
            if (mPlayList.value != null) {
                val song = mPlayList.value!![it]
                prepareMusic(song)
            }
        }
    }

    private lateinit var mApplication: Application

    private var mPositionUpdateJob: Job? = null

    private var mMediaPlayer: ExoPlayer? = null

    @Synchronized
    private fun createExoPlayer(): ExoPlayer {
        val exoPlayer = ExoPlayer.Builder(mApplication).build()
        exoPlayer.addListener(this)
        return exoPlayer
    }

    //初始化媒体播放器
    @Synchronized
    private fun initMediaPlayer() {
        if (mMediaPlayer != null) return
        mMediaPlayer = createExoPlayer()
        val process = mProgress.value
        prepareMusic(mChangeMusic.value ?: return)
        seekTo(process ?: return)
    }

    private fun prepareMusic(song: SongBean) {
        initMediaPlayer()

        mProgress.value = 0
        mChangeMusic.value = song
        mMediaPlayer?.let {
            it.setMediaItem(MediaItem.fromUri(song.data))
            it.prepare()
            it.playWhenReady = true
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

    override fun stop() {
        mPositionUpdateJob?.cancel()
        mPositionUpdateJob = null
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    /**
     * Exoplayer 部分
     */

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        mPause.value = !isPlaying
        if (isPlaying && mPositionUpdateJob == null) {
            mPositionUpdateJob = GlobalScope.launch(Dispatchers.Main) {
                while (true) {
                    mProgress.value = mMediaPlayer?.currentPosition?.toInt()
                    delay(1000)
                }
            }
        } else {
            mPositionUpdateJob?.cancel()
            mPositionUpdateJob = null
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_READY -> {
                mDuration.value = mMediaPlayer?.duration?.toInt()
            }

            Player.STATE_BUFFERING -> {
            }

            Player.STATE_ENDED -> {
                skipToNext()
            }

            Player.STATE_IDLE -> {
            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        skipToNext()
    }

}