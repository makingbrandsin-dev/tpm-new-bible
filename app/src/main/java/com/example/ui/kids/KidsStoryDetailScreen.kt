package com.example.ui.kids

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KidsStory
import com.example.ui.BibleViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KidsStoryDetailScreen(
    story: KidsStory,
    currentPageIndex: Int,
    viewModel: BibleViewModel,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPages = story.pages.size
    val currentPage = story.pages[(currentPageIndex - 1).coerceIn(0, totalPages - 1)]
    val currentLang by viewModel.currentLanguage.collectAsState()
    val exoState by viewModel.exoPlaybackState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .statusBarsPadding()
            .testTag("kids_story_detail_screen")
    ) {
        // Top Header Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
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
                text = "${story.title(currentLang)} (${currentPageIndex}/$totalPages)",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CelestialGold
            )

            // Page Dots indicator
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(totalPages) { idx ->
                    Box(
                        modifier = Modifier
                            .size(if (idx + 1 == currentPageIndex) 10.dp else 6.dp)
                            .background(
                                color = if (idx + 1 == currentPageIndex) CelestialGold else OnSurfaceVariantMuted,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        // Story Page Content with Animated Transitions & Animated River Chatter
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

            AnimatedRiverChatterView(
                story = story,
                page = page,
                totalPages = totalPages,
                currentLang = currentLang,
                playbackState = exoState,
                onLanguageChange = { viewModel.setLanguage(it) },
                onPlayAudio = {
                    viewModel.playKidsStoryNarration(story.title(currentLang), page.pageNumber)
                },
                onPauseAudio = {
                    viewModel.toggleAudioPlayPause()
                },
                onNextPage = onNextPage,
                onPrevPage = onPrevPage,
                onStartQuiz = {
                    viewModel.startStoryQuiz(story)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
