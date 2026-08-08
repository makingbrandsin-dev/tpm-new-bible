package com.example.ui.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaItem
import com.example.ui.BibleViewModel
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.MutedOutlineVariant
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh

@Composable
fun AudioPlayerScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val audioState by viewModel.audioState.collectAsState()
    val mediaList = viewModel.repository.getSampleMedia()
    val currentItem = audioState.currentItem ?: mediaList.first()

    var playbackSpeed by remember { mutableStateOf("1.0x") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("audio_player_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Player Hero Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Album Art
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1.2f)
                ) {
                    AsyncImage(
                        model = currentItem.thumbnailUrl,
                        contentDescription = currentItem.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Title & Artist
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = currentItem.title,
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight
                    )
                    Text(
                        text = currentItem.speakerOrArtist,
                        fontSize = 14.sp,
                        color = OnSurfaceVariantMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Audio Slider Timeline
                Column(modifier = Modifier.fillMaxWidth()) {
                    Slider(
                        value = audioState.progress,
                        onValueChange = { },
                        colors = SliderDefaults.colors(
                            thumbColor = CelestialGold,
                            activeTrackColor = CelestialGold,
                            inactiveTrackColor = SurfaceContainer
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = audioState.currentTimeStr, fontSize = 12.sp, color = OnSurfaceVariantMuted)
                        Text(text = audioState.totalTimeStr, fontSize = 12.sp, color = OnSurfaceVariantMuted)
                    }
                }

                // Playback Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        playbackSpeed = when (playbackSpeed) {
                            "0.75x" -> "1.0x"
                            "1.0x" -> "1.25x"
                            "1.25x" -> "1.5x"
                            else -> "0.75x"
                        }
                    }) {
                        Text(
                            text = playbackSpeed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialGold
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "Rewind 10s",
                            tint = CelestialGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleAudioPlayPause() },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(CelestialGold)
                            .testTag("audio_screen_play_pause")
                    ) {
                        Icon(
                            imageVector = if (audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = OnPrimaryDark,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "Forward 10s",
                            tint = CelestialGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Equalizer",
                            tint = CelestialGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = MutedOutlineVariant.copy(alpha = 0.3f))

        // Playlist & Audio Chapters
        Text(
            text = "Audio Sanctuary & Sermons",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurfaceLight
        )

        mediaList.forEach { media ->
            val isCurrent = media.id == currentItem.id

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isCurrent) SurfaceContainerHigh else SurfaceContainer)
                    .border(
                        1.dp,
                        if (isCurrent) CelestialGold else androidx.compose.ui.graphics.Color.Transparent,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { viewModel.playMediaItem(media) }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        AsyncImage(
                            model = media.thumbnailUrl,
                            contentDescription = media.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = media.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) CelestialGold else OnSurfaceLight
                        )
                        Text(
                            text = "${media.speakerOrArtist} • ${media.duration}",
                            fontSize = 12.sp,
                            color = OnSurfaceVariantMuted
                        )
                    }

                    Icon(
                        imageVector = if (isCurrent && audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = CelestialGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
