package com.example.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AiBibleAssistant
import com.example.data.AiVerseResult
import com.example.ui.BibleViewModel
import com.example.ui.MainTab
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.MutedOutlineVariant
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiVerseAssistantSection(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var activeTopicTitle by remember { mutableStateOf("Peace & Anxiety") }
    var verseResults by remember { mutableStateOf<List<AiVerseResult>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Initial load for default topic
    LaunchedEffect(Unit) {
        isLoading = true
        verseResults = AiBibleAssistant.findVersesForTopic(activeTopicTitle)
        isLoading = false
    }

    fun executeSearch(topicToSearch: String) {
        if (topicToSearch.isBlank()) return
        keyboardController?.hide()
        activeTopicTitle = topicToSearch
        scope.launch {
            isLoading = true
            verseResults = AiBibleAssistant.findVersesForTopic(topicToSearch)
            isLoading = false
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ai_verse_assistant_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CelestialGold.copy(alpha = 0.2f))
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Assistant",
                        tint = CelestialGold,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AI Bible Verse Assistant",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight
                    )
                    Text(
                        text = "Find all related KJV verses for any topic or situation",
                        fontSize = 12.sp,
                        color = OnSurfaceVariantMuted
                    )
                }
            }

            HorizontalDivider(color = MutedOutlineVariant.copy(alpha = 0.3f))

            // Preset Topic Chips (Horizontal Scroll)
            Text(
                text = "Popular Topics:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CelestialGold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(AiBibleAssistant.quickTopics) { topic ->
                    val isSelected = activeTopicTitle.equals(topic, ignoreCase = true)
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) CelestialGold else SurfaceDark,
                        contentColor = if (isSelected) OnPrimaryDark else OnSurfaceLight,
                        modifier = Modifier
                            .clickable {
                                searchQuery = topic
                                executeSearch(topic)
                            }
                    ) {
                        Text(
                            text = topic,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // Custom Topic Search Input Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search topic (e.g. patience, fear)...",
                            fontSize = 13.sp,
                            color = OnSurfaceVariantMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = CelestialGold,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = OnSurfaceVariantMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { executeSearch(searchQuery) }),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedBorderColor = CelestialGold,
                        unfocusedBorderColor = MutedOutlineVariant.copy(alpha = 0.5f),
                        focusedTextColor = OnSurfaceLight,
                        unfocusedTextColor = OnSurfaceLight
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_topic_search_input")
                )

                Button(
                    onClick = { executeSearch(searchQuery.ifBlank { activeTopicTitle }) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CelestialGold,
                        contentColor = OnPrimaryDark
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    modifier = Modifier.testTag("ai_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Ask AI",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Results Heading / Loading State
            if (isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        color = CelestialGold,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Finding KJV verses for '$activeTopicTitle'...",
                        fontSize = 13.sp,
                        color = CelestialGold
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "KJV Verses for \"$activeTopicTitle\"",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestialGold
                    )
                    Surface(
                        shape = CircleShape,
                        color = SurfaceDark
                    ) {
                        Text(
                            text = "${verseResults.size} verses found",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                // List of Verse Cards
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    verseResults.forEach { verse ->
                        AiVerseItemCard(
                            verse = verse,
                            onReadInBible = {
                                viewModel.selectBook(verse.book)
                                viewModel.selectChapter(verse.chapter)
                                viewModel.setTab(MainTab.BIBLE)
                            },
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Verse", "${verse.text}\n— ${verse.reference}")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied ${verse.reference} to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onShare = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    putExtra(Intent.EXTRA_TEXT, "✝️ KJV Verse on $activeTopicTitle\n\n\"${verse.text}\"\n\n— ${verse.reference}\n\nShared via TPM Bible App")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Verse"))
                            },
                            onWhatsAppShare = {
                                val message = "✝️ KJV Verse on $activeTopicTitle\n\n\"${verse.text}\"\n\n— ${verse.reference}\n\nShared via TPM Bible App"
                                val waIntent = Intent(Intent.ACTION_SEND).apply {
                                    putExtra(Intent.EXTRA_TEXT, message)
                                    type = "text/plain"
                                    setPackage("com.whatsapp")
                                }
                                try {
                                    context.startActivity(waIntent)
                                } catch (e: Exception) {
                                    val chooser = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                                        putExtra(Intent.EXTRA_TEXT, message)
                                        type = "text/plain"
                                    }, "Share via WhatsApp")
                                    context.startActivity(chooser)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AiVerseItemCard(
    verse: AiVerseResult,
    onReadInBible: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onWhatsAppShare: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceDark
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = verse.reference,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelestialGold
                )

                if (verse.theme.isNotEmpty()) {
                    Surface(
                        shape = CircleShape,
                        color = CelestialGold.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = verse.theme,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CelestialGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "\"${verse.text}\"",
                fontFamily = PlayfairDisplayFontFamily,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = OnSurfaceLight
            )

            HorizontalDivider(color = MutedOutlineVariant.copy(alpha = 0.2f), modifier = Modifier.padding(top = 4.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Read in Reader
                Button(
                    onClick = onReadInBible,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CelestialGold.copy(alpha = 0.2f),
                        contentColor = CelestialGold
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Read in Bible",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Read",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Copy
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Verse",
                            tint = OnSurfaceVariantMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Share
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Verse",
                            tint = OnSurfaceVariantMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // WhatsApp
                    IconButton(
                        onClick = onWhatsAppShare,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "WhatsApp Share",
                            tint = Color(0xFF25D366),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
