package com.example.ui.audio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.BibleBookInfo
import com.example.data.BibleCatalog
import com.example.model.AppLanguage
import com.example.ui.BibleViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val selectedBookName by viewModel.selectedBook.collectAsState()
    val selectedChapter by viewModel.selectedChapter.collectAsState()
    val exoState by viewModel.exoPlaybackState.collectAsState()

    val activeBookInfo = remember(selectedBookName) { BibleCatalog.findBook(selectedBookName) }

    var showBookPickerModal by remember { mutableStateOf(false) }
    var selectedSpeed by remember { mutableStateOf(1.0f) }
    var selectedTimerMinutes by remember { mutableStateOf(0) }
    var showTimerDialog by remember { mutableStateOf(false) }

    // Book Picker Modal Sheet
    if (showBookPickerModal) {
        ModalBottomSheet(
            onDismissRequest = { showBookPickerModal = false },
            containerColor = SurfaceDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Select Bible Book",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelestialGold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                var testFilter by remember { mutableStateOf("All") }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Old", "New").forEach { testament ->
                        FilterChip(
                            selected = testFilter == testament,
                            onClick = { testFilter = testament },
                            label = { Text(if (testament == "All") "All Books (66)" else "$testament Testament") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CelestialGold,
                                selectedLabelColor = OnPrimaryDark,
                                containerColor = SurfaceContainer
                            )
                        )
                    }
                }

                val filteredBooks = remember(testFilter) {
                    if (testFilter == "All") BibleCatalog.BOOKS
                    else BibleCatalog.BOOKS.filter { it.testament == testFilter }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredBooks.forEach { book ->
                        val isSelected = book.id.equals(selectedBookName, ignoreCase = true)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectBook(book.id)
                                    showBookPickerModal = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) SurfaceContainerHigh else SurfaceContainer
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, CelestialGold) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = book.nameForLanguage(currentLang),
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) CelestialGold else OnSurfaceLight
                                    )
                                    Text(
                                        text = "${book.nameEnglish} • ${book.testament} Testament • ${book.totalChapters} Ch",
                                        fontSize = 11.sp,
                                        color = OnSurfaceVariantMuted
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = CelestialGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Sleep Timer Dialog
    if (showTimerDialog) {
        AlertDialog(
            onDismissRequest = { showTimerDialog = false },
            title = { Text("Set Audio Sleep Timer") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0 to "Off", 15 to "15 Minutes", 30 to "30 Minutes", 45 to "45 Minutes", 60 to "60 Minutes").forEach { (mins, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedTimerMinutes = mins
                                    viewModel.audioPlayerManager.setSleepTimer(mins.toLong())
                                    showTimerDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTimerMinutes == mins,
                                onClick = {
                                    selectedTimerMinutes = mins
                                    viewModel.audioPlayerManager.setSleepTimer(mins.toLong())
                                    showTimerDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTimerDialog = false }) {
                    Text("Close", color = CelestialGold)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("audio_player_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Interactive Book & Chapter Chooser
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "SELECT SCRIPTURE PORTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = CelestialGold
                )

                // Select Book Button
                Surface(
                    onClick = { showBookPickerModal = true },
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CelestialGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = activeBookInfo.nameForLanguage(currentLang),
                                fontFamily = FontFamily.Serif,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = CelestialGold
                            )
                            Text(
                                text = "${activeBookInfo.nameEnglish} • ${activeBookInfo.testament} Testament (${activeBookInfo.totalChapters} Chapters)",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Book",
                            tint = CelestialGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Chapter Selector Horizontal List
                Column {
                    Text(
                        text = "Chapter: $selectedChapter",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceLight,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items((1..activeBookInfo.totalChapters).toList()) { chapNum ->
                            val isSelected = chapNum == selectedChapter
                            Surface(
                                onClick = {
                                    viewModel.selectChapter(chapNum)
                                    viewModel.playCurrentChapterAudio()
                                },
                                shape = CircleShape,
                                color = if (isSelected) CelestialGold else SurfaceDark,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$chapNum",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) OnPrimaryDark else OnSurfaceLight
                                    )
                                }
                            }
                        }
                    }
                }

                // Big Primary Action "Play Chapter Audio"
                Button(
                    onClick = { viewModel.playCurrentChapterAudio() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("play_audio_chapter_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CelestialGold, contentColor = OnPrimaryDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = if (exoState.isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                        contentDescription = "Play",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (exoState.isPlaying) "PAUSE AUDIO SCRIPTURE" else "PLAY CHAPTER ${selectedChapter} AUDIO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 3. Lockscreen Background Play Indicator Card
        Surface(
            color = CelestialGold.copy(alpha = 0.12f),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lockscreen",
                    tint = CelestialGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Background & Lock Screen Player Active",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestialGold
                    )
                    Text(
                        text = "Audio continues playing even when device screen is turned off or locked",
                        fontSize = 10.sp,
                        color = OnSurfaceVariantMuted
                    )
                }
            }
        }

        // 4. Main Audio Player Controller Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Artwork Image
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceDark)
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1509021436468-d5103e8b15d2?w=600&q=80",
                        contentDescription = "Scripture Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SurfaceDark.copy(alpha = 0.35f))
                    )

                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = CelestialGold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                    )
                }

                // Currently Playing Title & Subtitle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = exoState.currentTitle.ifEmpty { "${activeBookInfo.nameForLanguage(currentLang)} Chapter $selectedChapter" },
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight
                    )
                    Text(
                        text = exoState.currentSubtitle.ifEmpty { "Holy Bible Audio • ${currentLang.displayName}" },
                        fontSize = 12.sp,
                        color = OnSurfaceVariantMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Slider / Seek bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    Slider(
                        value = exoState.progressFraction,
                        onValueChange = { frac -> viewModel.audioPlayerManager.seekToFraction(frac) },
                        colors = SliderDefaults.colors(
                            thumbColor = CelestialGold,
                            activeTrackColor = CelestialGold,
                            inactiveTrackColor = SurfaceDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = exoState.currentTimeFormatted,
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted
                        )
                        Text(
                            text = exoState.totalTimeFormatted,
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted
                        )
                    }
                }

                // Playback Action Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Chapter
                    IconButton(
                        onClick = {
                            viewModel.changeChapter(-1)
                            viewModel.playCurrentChapterAudio()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Chapter",
                            tint = OnSurfaceLight,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // -10s Rewind
                    IconButton(
                        onClick = { viewModel.audioPlayerManager.seekRewind(10000L) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "-10 sec",
                            tint = CelestialGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Play/Pause Main Toggle
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(CelestialGold)
                            .clickable { viewModel.toggleAudioPlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (exoState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = OnPrimaryDark,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // +10s Forward
                    IconButton(
                        onClick = { viewModel.audioPlayerManager.seekForward(10000L) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "+10 sec",
                            tint = CelestialGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Next Chapter
                    IconButton(
                        onClick = {
                            viewModel.changeChapter(1)
                            viewModel.playCurrentChapterAudio()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Chapter",
                            tint = OnSurfaceLight,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                HorizontalDivider(color = MutedOutlineVariant.copy(alpha = 0.3f))

                // Playback Speed & Sleep Timer Toolbar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Speed selector
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speed",
                            tint = OnSurfaceVariantMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { spd ->
                            Text(
                                text = "${spd}x",
                                fontSize = 11.sp,
                                fontWeight = if (selectedSpeed == spd) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedSpeed == spd) CelestialGold else OnSurfaceVariantMuted,
                                modifier = Modifier
                                    .clickable {
                                        selectedSpeed = spd
                                        viewModel.audioPlayerManager.setPlaybackSpeed(spd)
                                    }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Sleep Timer
                    Surface(
                        onClick = { showTimerDialog = true },
                        shape = CircleShape,
                        color = SurfaceDark
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Sleep Timer",
                                tint = CelestialGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (selectedTimerMinutes > 0) "${selectedTimerMinutes}m" else "Timer",
                                fontSize = 11.sp,
                                color = CelestialGold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
