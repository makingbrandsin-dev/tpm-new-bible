package com.example.ui.bible

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VerseEntity
import com.example.model.AppLanguage
import com.example.ui.BibleViewModel
import com.example.ui.MainTab
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.MutedOutline
import com.example.ui.theme.MutedOutlineVariant
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh

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
    val selectedChapter by viewModel.selectedChapter.collectAsState()

    var showFontSheet by remember { mutableStateOf(false) }
    var showBookDropdown by remember { mutableStateOf(false) }

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
                .padding(horizontal = 16.dp, vertical = 10.dp),
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

                Box {
                    Text(
                        text = "John $selectedChapter",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = CelestialGold,
                        modifier = Modifier
                            .clickable { showBookDropdown = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("reader_book_chapter_title")
                    )

                    DropdownMenu(
                        expanded = showBookDropdown,
                        onDismissRequest = { showBookDropdown = false }
                    ) {
                        listOf(1, 2, 3, 4, 5).forEach { chap ->
                            DropdownMenuItem(
                                text = { Text("John $chap") },
                                onClick = {
                                    viewModel.changeChapter(chap - selectedChapter)
                                    showBookDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Row {
                IconButton(onClick = {
                    val nextLang = when (currentLang) {
                        AppLanguage.ENGLISH -> AppLanguage.TAMIL
                        AppLanguage.TAMIL -> AppLanguage.TELUGU
                        AppLanguage.TELUGU -> AppLanguage.ENGLISH
                    }
                    viewModel.setLanguage(nextLang)
                }) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Switch Language",
                        tint = CelestialGold
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

        // Reader Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Version and Title Header
            Text(
                text = "KING JAMES VERSION",
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                color = CelestialGold
            )

            Text(
                text = "The Gospel According to St. John",
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
                color = OnSurfaceLight
            )

            HorizontalDivider(
                color = MutedOutlineVariant.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Language Selection Chips inside reader
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppLanguage.entries.forEach { lang ->
                    FilterChip(
                        selected = currentLang == lang,
                        onClick = { viewModel.setLanguage(lang) },
                        label = { Text(lang.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CelestialGold,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            // Scripture Verses List
            verses.forEach { verse ->
                val isSelected = verse.id == selectedVerseId

                val verseText = when (currentLang) {
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
                            color = if (isSelected) CelestialGold else androidx.compose.ui.graphics.Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            viewModel.selectVerse(if (isSelected) null else verse.id)
                        }
                        .padding(16.dp)
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
                                        viewModel.setTab(MainTab.AUDIO)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Headset,
                                            contentDescription = "Listen",
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

            // Chapter Footer Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { viewModel.changeChapter(-1) },
                    shape = CircleShape,
                    modifier = Modifier.testTag("prev_chapter_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Chapter",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("← Luke 24", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.changeChapter(1) },
                    shape = CircleShape,
                    modifier = Modifier.testTag("next_chapter_button")
                ) {
                    Text("John ${selectedChapter + 1} →", fontSize = 13.sp)
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

    // Font Adjustment Bottom Sheet
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
