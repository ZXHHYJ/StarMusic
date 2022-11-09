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
import studio.mandysa.music.service.playmanager.bean.Song


/**
 * @author 黄浩
 */
object PlayManager {

    data class MusicInfo(
        val song: Song,
        val title: String,
        val coverUrl: String,
        val artist: Array<Song.NetworkBean.Artist>,
        val album: Song.NetworkBean.Album
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MusicInfo

            if (song != other.song) return false
            if (title != other.title) return false
            if (coverUrl != other.coverUrl) return false
            if (!artist.contentEquals(other.artist)) return false
            if (album != other.album) return false

            return true
        }

        override fun hashCode(): Int {
            var result = song.hashCode()
            result = 31 * result + title.hashCode()
            result = 31 * result + coverUrl.hashCode()
            result = 31 * result + artist.contentHashCode()
            result = 31 * result + album.hashCode()
            return result
        }

    }

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
    private val mChangeMusic = MutableLiveData<Song>()

    /**
     * 当前歌曲播放信息
     */
    private val mChangeMusicInfo = MutableLiveData<MusicInfo>()

    /**
     * 播放列表
     */
    private val mPlayList = MutableLiveData<List<Song>>()

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

    fun changePlayListLiveData(): LiveData<List<Song>> {
        return mPlayList
    }

    fun playingMusicProgressLiveData(): LiveData<Int> {
        return mProgress
    }

    fun playingMusicDurationLiveData(): LiveData<Int> {
        return mDuration
    }

    fun changeMusicLiveData(): LiveData<Song> {
        return mChangeMusic
    }

    fun changeMusicInfoLiveData(): LiveData<MusicInfo> {
        return mChangeMusicInfo
    }

    fun isPaused(): Boolean {
        return pauseLiveData().value!!
    }

    fun pauseLiveData(): LiveData<Boolean> {
        return mPause
    }

    @Suppress("UNCHECKED_CAST")
    fun addNextPlay(song: Song) {
        val list = mPlayList.value as ArrayList<Song>?
        list?.let {
            list.add(mIndex.value!! + 1, song)
            mPlayList.value = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun play(list: List<Song>, index: Int) {
        mPlayList.value = list
        updateIndex(index)
    }

    /**
     * 随机播放
     */
    fun shufflePlay(list: List<Song>, index: Int) {
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
        mHandler.post(mRunnable)
        mMediaPlayer!!.play()
    }

    fun pause() {
        if (!mMediaPlayer!!.isPlaying)
            return
        mHandler.removeCallbacks(mRunnable)
        mMediaPlayer!!.pause()
    }

    private fun playMusic(song: Song) {
        mProgress.value = 0
        mChangeMusic.value = song
        mChangeMusicInfo.value = when (song) {
            is Song.LocalBean -> {
                val coverUrl = "content://media/external/audio/albumart/${song.albumId}"
                MusicInfo(
                    song,
                    song.songName,
                    coverUrl,
                    arrayOf(Song.NetworkBean.Artist(song.artistId.toString(), song.artist)),
                    Song.NetworkBean.Album(
                        "",
                        coverUrl,
                        song.album,
                        ""
                    )
                )
            }
            is Song.NetworkBean -> MusicInfo(
                song,
                song.title,
                song.coverUrl,
                song.artist,
                song.album
            )
        }
        mMediaPlayer?.run {
            when (song) {
                is Song.LocalBean -> {
                    setMediaItem(MediaItem.fromUri(song.data))
                    prepare()
                    // TODO: 未测试
                }
                is Song.NetworkBean -> {
                    val url = MUSIC_URL + song.id
                    setMediaItem(MediaItem.fromUri(url))
                    prepare()
                }
            }
        }
    }

    init {
        mIndex.observeForever { p1: Int ->
            if (mPlayList.value != null) {
                val song = mPlayList.value!![p1]
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