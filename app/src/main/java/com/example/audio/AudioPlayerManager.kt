package com.example.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem as Media3Item
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.model.MediaItem
import com.example.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ExoPlaybackState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val currentMediaId: String? = null,
    val currentTitle: String = "No Audio Selected",
    val currentSubtitle: String = "Select a chapter or sermon",
    val playbackSpeed: Float = 1.0f,
    val isBuffering: Boolean = false,
    val error: String? = null
) {
    val progressFraction: Float
        get() = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f

    val currentTimeFormatted: String
        get() = formatTime(currentPositionMs)

    val totalTimeFormatted: String
        get() = formatTime(durationMs)

    private fun formatTime(ms: Long): String {
        val totalSeconds = (ms / 1000).coerceAtLeast(0)
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

class AudioPlayerManager(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    private val _playbackState = MutableStateFlow(ExoPlaybackState())
    val playbackState: StateFlow<ExoPlaybackState> = _playbackState.asStateFlow()

    private val mediaControlReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            when (intent?.action) {
                NotificationHelper.ACTION_PLAY_PAUSE -> togglePlayPause()
                NotificationHelper.ACTION_REWIND -> seekRewind(10000L)
                NotificationHelper.ACTION_FORWARD -> seekForward(10000L)
                NotificationHelper.ACTION_STOP -> stopAndClear()
            }
        }
    }

    init {
        initExoPlayer()
        registerMediaReceiver()
    }

    private fun registerMediaReceiver() {
        try {
            val filter = IntentFilter().apply {
                addAction(NotificationHelper.ACTION_PLAY_PAUSE)
                addAction(NotificationHelper.ACTION_REWIND)
                addAction(NotificationHelper.ACTION_FORWARD)
                addAction(NotificationHelper.ACTION_STOP)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(mediaControlReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                context.registerReceiver(mediaControlReceiver, filter)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @OptIn(UnstableApi::class)
    private fun initExoPlayer() {
        if (exoPlayer != null) return
        try {
            exoPlayer = ExoPlayer.Builder(context.applicationContext).build().apply {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _playbackState.value = _playbackState.value.copy(
                            isPlaying = isPlaying,
                            durationMs = duration.coerceAtLeast(0L)
                        )
                        if (isPlaying) {
                            startProgressUpdateLoop()
                        } else {
                            progressJob?.cancel()
                        }
                        updateLockscreenNotification()
                    }

                    override fun onPlaybackStateChanged(playbackStateInt: Int) {
                        val buffering = playbackStateInt == Player.STATE_BUFFERING
                        val dur = duration.coerceAtLeast(0L)
                        _playbackState.value = _playbackState.value.copy(
                            isBuffering = buffering,
                            durationMs = if (dur > 0) dur else _playbackState.value.durationMs
                        )
                        updateLockscreenNotification()
                    }

                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        _playbackState.value = _playbackState.value.copy(
                            isPlaying = false,
                            error = error.localizedMessage ?: "Playback error"
                        )
                        NotificationHelper.cancelLockscreenMediaNotification(context)
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateLockscreenNotification() {
        val state = _playbackState.value
        if (state.currentMediaId != null) {
            NotificationHelper.updateLockscreenMediaNotification(
                context = context,
                title = state.currentTitle,
                subtitle = state.currentSubtitle,
                isPlaying = state.isPlaying
            )
        }
    }

    fun playMedia(mediaItem: MediaItem) {
        val player = exoPlayer ?: return
        try {
            _playbackState.value = _playbackState.value.copy(
                currentMediaId = mediaItem.id,
                currentTitle = mediaItem.title,
                currentSubtitle = mediaItem.speakerOrArtist,
                error = null
            )
            val media3Item = Media3Item.fromUri(mediaItem.audioUrl)
            player.setMediaItem(media3Item)
            player.prepare()
            player.play()
            updateLockscreenNotification()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playChapterAudio(title: String, subtitle: String, streamUrl: String) {
        val player = exoPlayer ?: return
        try {
            _playbackState.value = _playbackState.value.copy(
                currentMediaId = "chapter_$title",
                currentTitle = title,
                currentSubtitle = subtitle,
                error = null
            )
            val media3Item = Media3Item.fromUri(streamUrl)
            player.setMediaItem(media3Item)
            player.prepare()
            player.play()
            updateLockscreenNotification()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            if (player.playbackState == Player.STATE_ENDED) {
                player.seekTo(0)
            }
            player.play()
        }
        updateLockscreenNotification()
    }

    fun stopAndClear() {
        val player = exoPlayer ?: return
        player.stop()
        _playbackState.value = _playbackState.value.copy(
            isPlaying = false,
            currentPositionMs = 0L
        )
        NotificationHelper.cancelLockscreenMediaNotification(context)
    }

    fun seekToFraction(fraction: Float) {
        val player = exoPlayer ?: return
        val targetMs = (fraction * _playbackState.value.durationMs).toLong()
        player.seekTo(targetMs)
        _playbackState.value = _playbackState.value.copy(currentPositionMs = targetMs)
    }

    fun seekForward(ms: Long = 10000L) {
        val player = exoPlayer ?: return
        val newPos = (player.currentPosition + ms).coerceAtMost(player.duration)
        player.seekTo(newPos)
        _playbackState.value = _playbackState.value.copy(currentPositionMs = newPos)
    }

    fun seekRewind(ms: Long = 10000L) {
        val player = exoPlayer ?: return
        val newPos = (player.currentPosition - ms).coerceAtLeast(0L)
        player.seekTo(newPos)
        _playbackState.value = _playbackState.value.copy(currentPositionMs = newPos)
    }

    private var sleepTimerJob: Job? = null

    fun setSpeed(speed: Float) {
        val player = exoPlayer ?: return
        player.setPlaybackSpeed(speed)
        _playbackState.value = _playbackState.value.copy(playbackSpeed = speed)
    }

    fun setPlaybackSpeed(speed: Float) {
        setSpeed(speed)
    }

    fun setSleepTimer(minutes: Long) {
        sleepTimerJob?.cancel()
        if (minutes <= 0) return
        sleepTimerJob = scope.launch {
            delay(minutes * 60 * 1000L)
            stopAndClear()
        }
    }

    private fun startProgressUpdateLoop() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    val pos = player.currentPosition.coerceAtLeast(0L)
                    val dur = player.duration.coerceAtLeast(0L)
                    _playbackState.value = _playbackState.value.copy(
                        currentPositionMs = pos,
                        durationMs = if (dur > 0) dur else _playbackState.value.durationMs
                    )
                }
                delay(250)
            }
        }
    }

    fun release() {
        progressJob?.cancel()
        try {
            context.unregisterReceiver(mediaControlReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        NotificationHelper.cancelLockscreenMediaNotification(context)
        exoPlayer?.release()
        exoPlayer = null
    }
}
