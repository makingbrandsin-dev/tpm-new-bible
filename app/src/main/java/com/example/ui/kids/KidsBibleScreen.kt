package com.example.ui.kids

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppLanguage
import com.example.model.KidsStory
import com.example.ui.BibleViewModel
import com.example.ui.theme.*

@Composable
fun KidsBibleScreen(
    viewModel: BibleViewModel,
    onOpenStory: (KidsStory) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val quizScores by viewModel.quizScores.collectAsState()
    val allStories = viewModel.repository.getKidsStories()

    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Old Testament", "New Testament", "Miracles")

    val filteredStories = when (selectedCategory) {
        "Old Testament" -> allStories.filter { it.category == "Old Testament" }
        "New Testament" -> allStories.filter { it.category == "New Testament" }
        "Miracles" -> allStories.filter { it.category == "Miracles" }
        else -> allStories
    }

    val featuredStory = filteredStories.firstOrNull() ?: allStories.first()
    val remainingStories = filteredStories.drop(if (filteredStories.contains(featuredStory)) 1 else 0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .testTag("kids_bible_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Kids Hero Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = KidsOrange,
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    text = "Sunday School Stories",
                    fontFamily = FontFamily.Serif,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelestialGold
                )

                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = KidsBlue,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = "Learn Bible stories with Teacher Grace & earn quiz marks!",
                fontSize = 13.sp,
                color = OnSurfaceVariantMuted,
                modifier = Modifier.padding(top = 4.dp)
            )



            // Category Tabs
            LazyRow(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CelestialGold,
                            selectedLabelColor = OnPrimaryDark,
                            containerColor = SurfaceContainer,
                            labelColor = OnSurfaceLight
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = CelestialGold.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }

        // Featured Story Bento Card
        featuredStory.let { story ->
            val storyScore = quizScores[story.id]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("featured_kids_story_card")
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.dp, KidsBlue.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                        .clickable { onOpenStory(story) },
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                    ) {
                        AsyncImage(
                            model = story.imageUrl,
                            contentDescription = story.title(currentLang),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            SurfaceDark.copy(alpha = 0.5f),
                                            SurfaceDark.copy(alpha = 0.95f)
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(18.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = KidsOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${story.category.uppercase()} • FEATURED",
                                    fontSize = 11.sp,
                                    letterSpacing = 1.2.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CelestialGold
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = story.title(currentLang),
                                fontFamily = FontFamily.Serif,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceLight
                            )

                            Text(
                                text = story.subtitle(currentLang),
                                fontSize = 13.sp,
                                color = OnSurfaceVariantMuted,
                                maxLines = 2,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            // Quiz Score Badge if completed
                            storyScore?.let { score ->
                                Surface(
                                    color = KidsGreen.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "Marks: ${score.scoreMarks}/100 ⭐ Grade: ${score.grade}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { onOpenStory(story) },
                                    colors = ButtonDefaults.buttonColors(containerColor = CelestialGold),
                                    shape = CircleShape,
                                    modifier = Modifier.testTag("read_featured_story_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = OnPrimaryDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Read Story",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnPrimaryDark
                                    )
                                }

                                OutlinedButton(
                                    onClick = { viewModel.startStoryQuiz(story) },
                                    shape = CircleShape
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = CelestialGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Take Quiz 🎓",
                                        fontSize = 12.sp,
                                        color = CelestialGold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Remaining Stories List
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "All Bible Adventures",
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceLight
            )

            remainingStories.forEach { story ->
                val badgeColor = Color(story.badgeColorHex)
                val storyScore = quizScores[story.id]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, badgeColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .clickable { onOpenStory(story) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Story Image Thumbnail
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            AsyncImage(
                                model = story.imageUrl,
                                contentDescription = story.title(currentLang),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Details
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = story.title(currentLang),
                                fontFamily = FontFamily.Serif,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceLight
                            )
                            Text(
                                text = story.subtitle(currentLang),
                                fontSize = 12.sp,
                                color = OnSurfaceVariantMuted,
                                maxLines = 2,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            // Quiz Score Indicator
                            storyScore?.let { score ->
                                Text(
                                    text = "Marks: ${score.scoreMarks}/100 Marks ⭐ Grade ${score.grade}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidsGreen,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    color = CelestialGold.copy(alpha = 0.2f),
                                    shape = CircleShape,
                                    modifier = Modifier.clickable { onOpenStory(story) }
                                ) {
                                    Text(
                                        text = "Read 📖",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CelestialGold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    color = KidsGreen.copy(alpha = 0.2f),
                                    shape = CircleShape,
                                    modifier = Modifier.clickable { viewModel.startStoryQuiz(story) }
                                ) {
                                    Text(
                                        text = "Take Quiz 🎓",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KidsGreen,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
