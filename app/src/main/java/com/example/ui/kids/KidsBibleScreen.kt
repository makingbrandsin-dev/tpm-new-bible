package com.example.ui.kids

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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.KidsBlue
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPink
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceDark

@Composable
fun KidsBibleScreen(
    viewModel: BibleViewModel,
    onOpenStory: (KidsStory) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val stories = viewModel.repository.getKidsStories()
    val featuredStory = stories.first()
    val otherStories = stories.drop(1)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("kids_bible_screen"),
        verticalArrangement = Arrangement.spacedBy(24.dp)
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
                    text = "Kids Bible",
                    fontFamily = FontFamily.Serif,
                    fontSize = 38.sp,
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
                text = "Discover the amazing stories of the Bible through fun adventures!",
                fontSize = 14.sp,
                color = OnSurfaceVariantMuted,
                modifier = Modifier.padding(top = 6.dp)
            )

            // Playful Language Chips
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AppLanguage.entries.forEach { lang ->
                    val isSelected = currentLang == lang
                    val chipColor = when (lang) {
                        AppLanguage.ENGLISH -> CelestialGold
                        AppLanguage.TAMIL -> KidsGreen
                        AppLanguage.TELUGU -> KidsPink
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) chipColor.copy(alpha = 0.25f) else SurfaceContainer)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) chipColor else chipColor.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                            .clickable { viewModel.setLanguage(lang) }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = lang.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) chipColor else OnSurfaceLight
                        )
                    }
                }
            }
        }

        // Featured Story Bento Card (Noah's Ark)
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
                    .clickable { onOpenStory(featuredStory) },
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    AsyncImage(
                        model = featuredStory.imageUrl,
                        contentDescription = featuredStory.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        SurfaceDark.copy(alpha = 0.6f),
                                        SurfaceDark.copy(alpha = 0.95f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
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
                                text = "FEATURED ADVENTURE",
                                fontSize = 11.sp,
                                letterSpacing = 1.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = CelestialGold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = featuredStory.title,
                            fontFamily = FontFamily.Serif,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceLight
                        )

                        Text(
                            text = featuredStory.subtitle,
                            fontSize = 13.sp,
                            color = OnSurfaceVariantMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onOpenStory(featuredStory) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CelestialGold,
                                contentColor = OnPrimaryDark
                            ),
                            shape = CircleShape,
                            modifier = Modifier.testTag("read_featured_story_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Read Story",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Read Story",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Accent New Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(KidsOrange)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NewReleases,
                    contentDescription = "New",
                    tint = SurfaceDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // More Stories Section
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = KidsGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "More Stories",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight
                    )
                }

                OutlinedButton(
                    onClick = { },
                    shape = CircleShape
                ) {
                    Text(text = "View All", fontSize = 11.sp, color = CelestialGold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = CelestialGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            HorizontalDivider(color = KidsGreen.copy(alpha = 0.2f))

            // Grid 2 Columns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                otherStories.forEach { story ->
                    val badgeColor = Color(story.badgeColorHex)

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.5.dp, badgeColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .clickable { onOpenStory(story) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            ) {
                                AsyncImage(
                                    model = story.imageUrl,
                                    contentDescription = story.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, SurfaceContainer)
                                            )
                                        )
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .clip(CircleShape)
                                        .background(badgeColor)
                                        .padding(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (story.badgeIcon == "bolt") Icons.Default.Bolt else Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = SurfaceDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = story.title,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceLight
                                )
                                Text(
                                    text = story.subtitle,
                                    fontSize = 12.sp,
                                    color = OnSurfaceVariantMuted,
                                    maxLines = 2,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
