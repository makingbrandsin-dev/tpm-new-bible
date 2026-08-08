package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.ExoPlaybackState
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainerHigh

@Composable
fun FloatingAudioDock(
    exoState: ExoPlaybackState,
    onTogglePlayPause: () -> Unit,
    onOpenFullPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    fallbackTitle: String = "TPM Audio Bible",
    fallbackSubtitle: String = "Tap play to listen • KJV Audio"
) {
    val isLoaded = exoState.currentMediaId != null
    val displayTitle = if (isLoaded) exoState.currentTitle else fallbackTitle
    val displaySubtitle = if (isLoaded) {
        "${exoState.currentSubtitle} • ${exoState.currentTimeFormatted} / ${exoState.totalTimeFormatted}"
    } else {
        fallbackSubtitle
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenFullPlayer() }
            .testTag("floating_audio_dock"),
        color = SurfaceContainerHigh,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Column {
            if (isLoaded) {
                LinearProgressIndicator(
                    progress = { exoState.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = CelestialGold,
                    trackColor = SurfaceContainerHigh
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CelestialGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Headset,
                        contentDescription = "Audio Player",
                        tint = CelestialGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = displayTitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = displaySubtitle,
                        fontSize = 11.sp,
                        color = OnSurfaceVariantMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CelestialGold)
                        .testTag("dock_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (exoState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (exoState.isPlaying) "Pause" else "Play",
                        tint = OnPrimaryDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
