package studio.mandysa.music.service.playmanager

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
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
                pause = !isPlaying
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        this@PlayManager.duration = this@apply.duration.toInt()
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
            progress = mMediaPlayer?.currentPosition?.toInt() ?: return
            mTimeHandler.postDelayed(this, 1000)
        }
    }

    private val mTimeHandler = Handler(Looper.myLooper()!!)

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
    var changeMusic by mutableStateOf<MetaMusic<*, *>?>(null)
        private set

    /**
     * 播放列表
     */
    var playlist by mutableStateOf<List<MetaMusic<*, *>>?>(null)
        private set

    /**
     * 播放状态
     */
    var pause by mutableStateOf<Boolean?>(null)
        private set

    /**
     * 当前播放歌曲的下标
     */
    var index by mutableStateOf(0)
        private set

    /**
     * 当前播放歌曲进度
     */
    var progress by mutableStateOf<Int?>(null)
        private set

    /**
     * 当前播放歌曲时长
     */
    var duration by mutableStateOf<Int?>(null)
        private set

    @Suppress("UNCHECKED_CAST")
    fun addNextPlay(model: MetaMusic<*, *>) {
        val list = playlist as ArrayList<MetaMusic<MetaArtist, MetaAlbum>>?
        list?.let {
            list.add(index + 1, model as MetaMusic<MetaArtist, MetaAlbum>)
            playlist = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun play(list: List<*>, index: Int) {
        playlist = list as List<MetaMusic<MetaArtist, MetaAlbum>>
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
        progress = position
        mMediaPlayer!!.seekTo(position.toLong())
    }

    private fun updateIndex(newIndex: Int) {
        if (!(newIndex >= 0 && playlist != null && newIndex <= playlist!!.size - 1)) {
            pause()
            return
        }
        index = newIndex
        if (playlist != null) {
            val metaMusic: MetaMusic<*, *> = playlist!![index]
            playMusic(metaMusic)
        }
    }

    fun skipToPrevious() {
        updateIndex(index - 1)
    }

    fun skipToNext() {
        updateIndex(index + 1)
    }

    fun play() {
        if (mMediaPlayer!!.isPlaying)
            return
        mTimeHandler.post(mRunnable)
        mMediaPlayer!!.play()
    }

    fun pause() {
        if (!mMediaPlayer!!.isPlaying)
            return
        mTimeHandler.removeCallbacks(mRunnable)
        mMediaPlayer!!.pause()
    }

    private fun playMusic(metaMusic: MetaMusic<*, *>) {
        progress = 0
        changeMusic = metaMusic
        changeMusic = metaMusic
        mMediaPlayer?.run {
            val url = MUSIC_URL + metaMusic.id
            setMediaItem(MediaItem.fromUri(url.toUri()))
            prepare()
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