package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

object NotificationHelper {

    private const val CHANNEL_ID = "tpm_bible_daily_verse"
    private const val CHANNEL_NAME = "TPM Bible Daily Verses"
    private const val CHANNEL_DESC = "Daily scripture verses and notifications from TPM Bible"

    const val MEDIA_CHANNEL_ID = "tpm_audio_lockscreen_player"
    const val MEDIA_CHANNEL_NAME = "Audio Sanctuary Lockscreen Player"
    const val MEDIA_NOTIFICATION_ID = 2002

    const val ACTION_PLAY_PAUSE = "com.example.tpmbible.ACTION_PLAY_PAUSE"
    const val ACTION_REWIND = "com.example.tpmbible.ACTION_REWIND"
    const val ACTION_FORWARD = "com.example.tpmbible.ACTION_FORWARD"
    const val ACTION_STOP = "com.example.tpmbible.ACTION_STOP"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val verseChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = CHANNEL_DESC
            }
            notificationManager.createNotificationChannel(verseChannel)

            val mediaChannel = NotificationChannel(
                MEDIA_CHANNEL_ID,
                MEDIA_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Lockscreen background audio player controls"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(mediaChannel)
        }
    }

    fun showDailyVerseNotification(
        context: Context,
        title: String = "TPM Bible • Daily Verse",
        verseText: String = "\"The Lord is my shepherd; I shall not want.\" — Psalm 23:1"
    ) {
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(verseText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(verseText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1001, builder.build())
    }

    fun updateLockscreenMediaNotification(
        context: Context,
        title: String,
        subtitle: String,
        isPlaying: Boolean
    ) {
        createNotificationChannel(context)

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        fun createBroadcastPendingIntent(actionStr: String, reqCode: Int): PendingIntent {
            val intent = Intent(actionStr).apply {
                setPackage(context.packageName)
            }
            return PendingIntent.getBroadcast(
                context, reqCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val playPauseIntent = createBroadcastPendingIntent(ACTION_PLAY_PAUSE, 1)
        val rewindIntent = createBroadcastPendingIntent(ACTION_REWIND, 2)
        val forwardIntent = createBroadcastPendingIntent(ACTION_FORWARD, 3)
        val stopIntent = createBroadcastPendingIntent(ACTION_STOP, 4)

        val playPauseIcon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        val playPauseTitle = if (isPlaying) "Pause" else "Play"

        val builder = NotificationCompat.Builder(context, MEDIA_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle(title)
            .setContentText("$subtitle • Background Lockscreen Audio")
            .setSubText("TPM Lockscreen Player 🎧")
            .setContentIntent(openAppPendingIntent)
            .setOngoing(isPlaying)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Visible on Lock Screen!
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(android.R.drawable.ic_media_rew, "-10s", rewindIntent)
            .addAction(playPauseIcon, playPauseTitle, playPauseIntent)
            .addAction(android.R.drawable.ic_media_ff, "+10s", forwardIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(MEDIA_NOTIFICATION_ID, builder.build())
    }

    fun cancelLockscreenMediaNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(MEDIA_NOTIFICATION_ID)
    }
}
