package com.example.ui.kids

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.model.KidsStory
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.KidsBlue
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.PrimaryContainerGold
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceDark

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KidsStoryDetailScreen(
    story: KidsStory,
    currentPageIndex: Int,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPages = story.pages.size
    val currentPage = story.pages[(currentPageIndex - 1).coerceIn(0, totalPages - 1)]

    var isNarrating by remember { mutableStateOf(false) }
    var selectedQuizOption by remember { mutableIntStateOf(-1) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(16.dp)
            .testTag("kids_story_detail_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = CelestialGold
                )
            }

            Text(
                text = "${story.title} (${currentPageIndex}/$totalPages)",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CelestialGold
            )

            IconButton(onClick = { isNarrating = !isNarrating }) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Audio Narration",
                    tint = if (isNarrating) KidsOrange else OnSurfaceVariantMuted
                )
            }
        }

        // Story Page Content with Animated Transitions
        AnimatedContent(
            targetState = currentPageIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            },
            modifier = Modifier.weight(1f)
        ) { targetPageIndex ->
            val page = story.pages[(targetPageIndex - 1).coerceIn(0, totalPages - 1)]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Large Story Image Banner
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
                ) {
                    AsyncImage(
                        model = page.imageUrl,
                        contentDescription = page.headline,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.2f)
                            .clip(RoundedCornerShape(24.dp))
                    )
                }

                Text(
                    text = page.headline,
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelestialGold
                )

                Text(
                    text = page.text,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = OnSurfaceLight
                )

                // Memory Verse Card
                if (page.memoryVerse != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryContainerGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = OnPrimaryDark,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = page.memoryVerse,
                                fontFamily = FontFamily.Serif,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimaryDark
                            )
                        }
                    }
                }

                // Fun Quiz Section if available
                if (page.quizQuestion != null && page.quizOptions != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "🧠 Fun Quiz: ${page.quizQuestion}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidsGreen
                            )

                            page.quizOptions.forEachIndexed { index, option ->
                                val isSelected = selectedQuizOption == index
                                val isCorrect = index == page.correctOptionIndex

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) {
                                                if (isCorrect) KidsGreen.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f)
                                            } else SurfaceDark
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) (if (isCorrect) KidsGreen else Color.Red) else CelestialGold.copy(alpha = 0.3f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedQuizOption = index }
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = option, fontSize = 14.sp, color = OnSurfaceLight)
                                        if (isSelected && isCorrect) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Correct",
                                                tint = KidsGreen,
                                                modifier = Modifier.size(18.dp)
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

        // Navigation Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPrevPage,
                enabled = currentPageIndex > 1,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prev")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous")
            }

            // Page Dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(totalPages) { idx ->
                    Box(
                        modifier = Modifier
                            .size(if (idx + 1 == currentPageIndex) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (idx + 1 == currentPageIndex) CelestialGold else OnSurfaceVariantMuted)
                    )
                }
            }

            Button(
                onClick = {
                    if (currentPageIndex < totalPages) {
                        onNextPage()
                    } else {
                        onClose()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CelestialGold,
                    contentColor = OnPrimaryDark
                ),
                shape = CircleShape
            ) {
                Text(if (currentPageIndex < totalPages) "Next" else "Done")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
            }
        }
    }
}
