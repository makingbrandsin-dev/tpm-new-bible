package com.example.ui.bible

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BibleCatalog
import com.example.model.AppLanguage
import com.example.ui.BibleViewModel
import com.example.ui.MainTab
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleReaderScreen(
    viewModel: BibleViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val verses by viewModel.currentVerses.collectAsState()
    val selectedVerseId by viewModel.selectedVerseId.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val fontSizeSp by viewModel.fontSizeSp.collectAsState()
    val isSerifFont by viewModel.isSerifFont.collectAsState()
    val selectedBookName by viewModel.selectedBook.collectAsState()
    val selectedChapter by viewModel.selectedChapter.collectAsState()
    val exoPlaybackState by viewModel.exoPlaybackState.collectAsState()

    var showFontSheet by remember { mutableStateOf(false) }
    var showBookPickerModal by remember { mutableStateOf(false) }
    var showChapterPickerModal by remember { mutableStateOf(false) }

    val activeBookInfo = remember(selectedBookName) { BibleCatalog.findBook(selectedBookName) }
    val fontFamily = if (isSerifFont) FontFamily.Serif else FontFamily.SansSerif

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("bible_reader_screen")
    ) {
        // Reader Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .padding(top = 4.dp, bottom = 2.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("reader_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = CelestialGold
                    )
                }

                // Book & Chapter Selector Pill
                Surface(
                    onClick = { showBookPickerModal = true },
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerHigh,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeBookInfo.nameForLanguage(currentLang)} $selectedChapter",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = CelestialGold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Book",
                            tint = CelestialGold
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Media3 ExoPlayer Audio Chapter Playback Button
                IconButton(
                    onClick = {
                        if (exoPlaybackState.isPlaying) {
                            viewModel.toggleAudioPlayPause()
                        } else {
                            viewModel.playCurrentChapterAudio()
                        }
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clip(CircleShape)
                        .background(if (exoPlaybackState.isPlaying) CelestialGold else SurfaceContainerHigh)
                ) {
                    Icon(
                        imageVector = if (exoPlaybackState.isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                        contentDescription = "Play Chapter Audio",
                        tint = if (exoPlaybackState.isPlaying) Color.White else CelestialGold
                    )
                }

                IconButton(
                    onClick = { showFontSheet = true },
                    modifier = Modifier.testTag("reader_font_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Font Settings",
                        tint = CelestialGold
                    )
                }
            }
        }

        // Language indicator banner
        Surface(
            color = CelestialGold.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reading: ${currentLang.displayName} (KJV Version)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CelestialGold
                )
                Text(
                    text = "Chapter $selectedChapter of ${activeBookInfo.totalChapters}",
                    fontSize = 12.sp,
                    color = OnSurfaceVariantMuted
                )
            }
        }

        // Reader Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Version and Title Header
            Text(
                text = "${activeBookInfo.testament.uppercase()} TESTAMENT • ${activeBookInfo.nameEnglish.uppercase()}",
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                color = CelestialGold
            )

            Text(
                text = "${activeBookInfo.nameForLanguage(currentLang)} - Chapter $selectedChapter",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 26.sp,
                color = OnSurfaceLight
            )

            HorizontalDivider(
                color = MutedOutlineVariant.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Scripture Verses List with smooth AnimatedContent transition
            AnimatedContent(
                targetState = Pair(selectedChapter, currentLang),
                transitionSpec = {
                    fadeIn(animationSpec = tween(280)) togetherWith fadeOut(animationSpec = tween(280))
                },
                label = "ScriptureTransition"
            ) { (_, lang) ->
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    verses.forEach { verse ->
                        val isSelected = verse.id == selectedVerseId

                        val verseText = when (lang) {
                            AppLanguage.ENGLISH -> verse.textEnglish
                            AppLanguage.TAMIL -> verse.textTamil
                            AppLanguage.TELUGU -> verse.textTelugu
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) SurfaceContainerHigh else SurfaceContainer.copy(alpha = 0.3f)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.dp,
                                    color = if (isSelected) CelestialGold else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    viewModel.selectVerse(if (isSelected) null else verse.id)
                                }
                                .padding(14.dp)
                                .testTag("verse_item_${verse.verseNumber}")
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "${verse.verseNumber}",
                                        fontSize = (fontSizeSp * 0.75f).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CelestialGold,
                                        modifier = Modifier.padding(end = 12.dp, top = 2.dp)
                                    )

                                    Text(
                                        text = verseText,
                                        fontFamily = fontFamily,
                                        fontSize = fontSizeSp.sp,
                                        lineHeight = (fontSizeSp * 1.6f).sp,
                                        color = OnSurfaceLight,
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                // Selected Verse Actions Toolbar
                                AnimatedVisibility(visible = isSelected) {
                                    Column(modifier = Modifier.padding(top = 12.dp)) {
                                        HorizontalDivider(color = MutedOutlineVariant)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            IconButton(onClick = { viewModel.toggleBookmark(verse) }) {
                                                Icon(
                                                    imageVector = if (verse.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                                    contentDescription = "Bookmark",
                                                    tint = CelestialGold
                                                )
                                            }
                                            IconButton(onClick = {
                                                viewModel.playCurrentChapterAudio()
                                                viewModel.setTab(MainTab.AUDIO)
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Headset,
                                                    contentDescription = "Listen with ExoPlayer",
                                                    tint = CelestialGold
                                                )
                                            }
                                            IconButton(onClick = { }) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy",
                                                    tint = CelestialGold
                                                )
                                            }
                                            IconButton(onClick = { }) {
                                                Icon(
                                                    imageVector = Icons.Default.Share,
                                                    contentDescription = "Share",
                                                    tint = CelestialGold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Chapter Footer Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { viewModel.changeChapter(-1) },
                    enabled = selectedChapter > 1,
                    shape = CircleShape,
                    modifier = Modifier.testTag("prev_chapter_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Chapter",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Previous", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { showChapterPickerModal = true },
                    shape = CircleShape
                ) {
                    Text("Chapter $selectedChapter / ${activeBookInfo.totalChapters}", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.changeChapter(1) },
                    enabled = selectedChapter < activeBookInfo.totalChapters,
                    shape = CircleShape,
                    modifier = Modifier.testTag("next_chapter_button")
                ) {
                    Text("Next", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Chapter",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    // Modal Sheet 1: Book Picker (All 66 Books)
    if (showBookPickerModal) {
        ModalBottomSheet(
            onDismissRequest = { showBookPickerModal = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            var selectedTestamentTab by remember { mutableStateOf("All") }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(500.dp)
            ) {
                Text(
                    text = "Select Bible Book",
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelestialGold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Old", "New").forEach { tab ->
                        FilterChip(
                            selected = selectedTestamentTab == tab,
                            onClick = { selectedTestamentTab = tab },
                            label = { Text(if (tab == "All") "All 66 Books" else "$tab Testament") }
                        )
                    }
                }

                val filteredBooks = remember(selectedTestamentTab) {
                    when (selectedTestamentTab) {
                        "Old" -> BibleCatalog.BOOKS.filter { it.testament == "Old" }
                        "New" -> BibleCatalog.BOOKS.filter { it.testament == "New" }
                        else -> BibleCatalog.BOOKS
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredBooks) { book ->
                        val isCurrent = book.id.equals(selectedBookName, ignoreCase = true)
                        Card(
                            onClick = {
                                viewModel.selectBook(book.id)
                                showBookPickerModal = false
                                showChapterPickerModal = true
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) CelestialGold else SurfaceContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = book.nameForLanguage(currentLang),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else OnSurfaceLight
                                )
                                Text(
                                    text = "${book.totalChapters} Chapters",
                                    fontSize = 11.sp,
                                    color = if (isCurrent) Color.White.copy(alpha = 0.8f) else OnSurfaceVariantMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet 2: Chapter Picker Grid
    if (showChapterPickerModal) {
        ModalBottomSheet(
            onDismissRequest = { showChapterPickerModal = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(420.dp)
            ) {
                Text(
                    text = "${activeBookInfo.nameForLanguage(currentLang)} - Select Chapter",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelestialGold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items((1..activeBookInfo.totalChapters).toList()) { chapNum ->
                        val isSelected = chapNum == selectedChapter
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) CelestialGold else SurfaceContainer)
                                .clickable {
                                    viewModel.selectChapter(chapNum)
                                    showChapterPickerModal = false
                                }
                        ) {
                            Text(
                                text = "$chapNum",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else OnSurfaceLight
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet 3: Font Adjustment
    if (showFontSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFontSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Reader Settings",
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelestialGold
                )

                Text(
                    text = "Font Size: ${fontSizeSp}sp",
                    fontSize = 14.sp,
                    color = OnSurfaceLight
                )

                Slider(
                    value = fontSizeSp.toFloat(),
                    onValueChange = { viewModel.setFontSize(it.toInt()) },
                    valueRange = 14f..28f,
                    colors = SliderDefaults.colors(
                        thumbColor = CelestialGold,
                        activeTrackColor = CelestialGold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Use Serif Font (Playfair Display)", color = OnSurfaceLight)
                    FilterChip(
                        selected = isSerifFont,
                        onClick = { viewModel.toggleSerifFont() },
                        label = { Text(if (isSerifFont) "Serif" else "Sans") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
