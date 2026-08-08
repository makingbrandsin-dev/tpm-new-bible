package com.example.ui.kids

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.audio.ExoPlaybackState
import com.example.model.AppLanguage
import com.example.model.KidsStory
import com.example.model.KidsStoryPage
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPink
import kotlin.math.sin

private val RiverDeepBlue = Color(0xFF0288D1)
private val RiverLightCyan = Color(0xFF4DD0E1)
private val RiverCurrentWhite = Color(0xAAFFFFFF)
private val FeatherGold = Color(0xFFFFB300)

@Composable
fun AnimatedRiverChatterView(
    story: KidsStory,
    page: KidsStoryPage,
    totalPages: Int,
    currentLang: AppLanguage,
    playbackState: ExoPlaybackState,
    onLanguageChange: (AppLanguage) -> Unit,
    onPlayAudio: () -> Unit,
    onPauseAudio: () -> Unit,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit,
    onStartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "riverTransition")

    // Smooth continuous river flow phase
    val riverWavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "riverWave"
    )

    // Secondary river wave phase offset
    val riverWavePhase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "riverWave2"
    )

    // Mouth animation for fish chatter
    val mouthAnim by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(
            animation = tween(160, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mouthAnim"
    )

    // Fish swimming bobbing motion
    val fishSwimBob by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fishSwimBob"
    )

    // Tail fin sway animation
    val tailSway by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tailSway"
    )

    // Side fin paddle animation
    val sideFinPaddle by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sideFinPaddle"
    )

    // Floating river bubbles rising
    val bubbleFloat1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubbleFloat1"
    )

    // Image model: Ensure story illustration stays identical to catalog story image if page image is blank
    val storyImageUrl = page.imageUrl.ifBlank { story.imageUrl }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F8FF))
            .verticalScroll(rememberScrollState())
    ) {
        // Top Animated Canvas Section (Same Story Image + Multi-layered Flowing River + Barnaby Fish)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .shadow(6.dp)
        ) {
            // Story Illustration Background Image (Guaranteed same image as story card)
            AsyncImage(
                model = storyImageUrl,
                contentDescription = page.headline(currentLang),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Flowing River Canvas Overlay with smooth water waves, currents & bubbles
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.20f),
                                Color.Black.copy(alpha = 0.65f)
                            )
                        )
                    )
            ) {
                val width = size.width
                val height = size.height

                // Deep River Layer
                val riverPath1 = Path().apply {
                    moveTo(0f, height * 0.62f)
                    val amp1 = 9f
                    for (x in 0..width.toInt() step 8) {
                        val y = height * 0.62f + sin((x * 0.012f) + riverWavePhase) * amp1
                        lineTo(x.toFloat(), y)
                    }
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }
                drawPath(
                    path = riverPath1,
                    brush = Brush.verticalGradient(
                        colors = listOf(RiverLightCyan.copy(alpha = 0.82f), RiverDeepBlue)
                    )
                )

                // Secondary Translucent Water Shimmer Wave
                val riverPath2 = Path().apply {
                    moveTo(0f, height * 0.70f)
                    val amp2 = 6f
                    for (x in 0..width.toInt() step 8) {
                        val y = height * 0.70f + sin((x * 0.018f) - riverWavePhase2) * amp2
                        lineTo(x.toFloat(), y)
                    }
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }
                drawPath(
                    path = riverPath2,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0x8080DEEA), Color(0xDD01579B))
                    )
                )

                // River Current Lines
                val currentPath = Path().apply {
                    val yBase = height * 0.78f
                    moveTo(0f, yBase)
                    for (x in 0..width.toInt() step 12) {
                        val y = yBase + sin((x * 0.022f) + riverWavePhase) * 5f
                        lineTo(x.toFloat(), y)
                    }
                }
                drawPath(
                    path = currentPath,
                    color = RiverCurrentWhite,
                    style = Stroke(width = 3.5f)
                )

                // Floating Bubbles moving up the river
                val bubble1Y = height * (0.92f - (bubbleFloat1 * 0.35f))
                val bubble1X = width * 0.35f + sin(bubbleFloat1 * 6f) * 15f
                drawCircle(
                    color = Color.White.copy(alpha = 0.6f),
                    radius = 8f,
                    center = Offset(bubble1X, bubble1Y)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.7f),
                    radius = 5f,
                    center = Offset(width * 0.65f - sin(bubbleFloat1 * 5f) * 12f, height * (0.95f - (bubbleFloat1 * 0.38f)))
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.5f),
                    radius = 11f,
                    center = Offset(width * 0.82f + sin(bubbleFloat1 * 4f) * 10f, height * (0.90f - (bubbleFloat1 * 0.30f)))
                )
            }

            // Language Selection Chips over top header
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                AppLanguage.entries.forEach { lang ->
                    val isSelected = currentLang == lang
                    Surface(
                        color = if (isSelected) CelestialGold else Color.Black.copy(alpha = 0.65f),
                        shape = CircleShape,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clickable { onLanguageChange(lang) }
                    ) {
                        Text(
                            text = lang.displayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.Black else Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Barnaby Fish Character & Speech Explanation Bubble
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speech Bubble explaining story to the kid
                Card(
                    modifier = Modifier
                        .widthIn(max = 160.dp)
                        .padding(end = 4.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, KidsOrange)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🐠 Barnaby says:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = KidsOrange
                        )
                        Text(
                            text = if (playbackState.isPlaying) "Listen closely, little friend! 📖✨" else "Tap play! Let's explore God's story! 🌊",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B5E20),
                            lineHeight = 14.sp
                        )
                    }
                }

                // Highly Animated Big Fish
                Box(
                    modifier = Modifier.graphicsLayer {
                        translationY = fishSwimBob
                    }
                ) {
                    BigFriendlyFishCharacter(
                        isPlayingAudio = playbackState.isPlaying,
                        mouthHeight = if (playbackState.isPlaying) mouthAnim else 4f,
                        tailSway = tailSway,
                        sideFinPaddle = sideFinPaddle
                    )
                }
            }

            // Audio Play Button Overlay
            IconButton(
                onClick = {
                    if (playbackState.isPlaying) onPauseAudio() else onPlayAudio()
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 14.dp, bottom = 12.dp)
                    .size(48.dp)
                    .shadow(6.dp, CircleShape)
                    .background(
                        Brush.horizontalGradient(listOf(KidsOrange, CelestialGold)),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Audio Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Story Details & Kid-friendly Teacher Explanation Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Headline & Page Tracker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = page.headline(currentLang),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Page ${page.pageNumber} / $totalPages",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            // Main Story Text Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = page.text(currentLang),
                    fontSize = 17.sp,
                    lineHeight = 26.sp,
                    color = Color(0xFF263238),
                    modifier = Modifier.padding(18.dp)
                )
            }

            // Teacher Grace Explaining the Story to Kid
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, KidsPink.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(KidsPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Teacher Grace",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Teacher Grace Explains 🍎",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = KidsPink
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = page.teacherNote(currentLang),
                            fontSize = 14.sp,
                            color = Color(0xFF4A148C),
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Memory Verse Pill
            page.memoryVerse(currentLang)?.let { verseText ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, CelestialGold.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFF57F17),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "✨ Memory Verse",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17)
                            )
                            Text(
                                text = verseText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF3E2723)
                            )
                        }
                    }
                }
            }

            // Page Navigation & Quiz Launch Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPrevPage,
                    enabled = page.pageNumber > 1,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                    Text("Previous")
                }

                if (page.pageNumber == totalPages) {
                    Button(
                        onClick = onStartQuiz,
                        colors = ButtonDefaults.buttonColors(containerColor = CelestialGold),
                        shape = CircleShape,
                        modifier = Modifier.testTag("start_quiz_button")
                    ) {
                        Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Take Teacher's Quiz & Get Marks 🎓⭐",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                } else {
                    Button(
                        onClick = onNextPage,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = CircleShape
                    ) {
                        Text("Next Page")
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
private fun BigFriendlyFishCharacter(
    isPlayingAudio: Boolean,
    mouthHeight: Float,
    tailSway: Float,
    sideFinPaddle: Float
) {
    Canvas(
        modifier = Modifier.size(135.dp)
    ) {
        val w = size.width
        val h = size.height

        // Tail Fin with swaying path
        val tailPath = Path().apply {
            moveTo(w * 0.32f, h * 0.50f)
            lineTo(w * 0.05f + tailSway, h * 0.20f)
            lineTo(w * 0.14f, h * 0.50f)
            lineTo(w * 0.05f + tailSway, h * 0.80f)
            close()
        }
        drawPath(path = tailPath, color = KidsOrange)

        // Top Fin
        val topFin = Path().apply {
            moveTo(w * 0.42f, h * 0.26f)
            quadraticTo(w * 0.55f, h * 0.05f, w * 0.70f, h * 0.28f)
            close()
        }
        drawPath(path = topFin, color = FeatherGold)

        // Main Vibrant Fish Body
        drawOval(
            color = Color(0xFFFF9800),
            topLeft = Offset(w * 0.24f, h * 0.22f),
            size = Size(w * 0.68f, h * 0.54f)
        )

        // Fish Belly Soft Yellow Highlight
        drawOval(
            color = Color(0xFFFFF59D),
            topLeft = Offset(w * 0.38f, h * 0.48f),
            size = Size(w * 0.44f, h * 0.22f)
        )

        // Side Fin with paddling effect
        val sideFin = Path().apply {
            moveTo(w * 0.52f, h * 0.54f)
            lineTo(w * 0.38f + sideFinPaddle, h * 0.74f)
            lineTo(w * 0.58f, h * 0.62f)
            close()
        }
        drawPath(path = sideFin, color = KidsPink)

        // Big Expressive Eye
        val eyeCenter = Offset(w * 0.73f, h * 0.40f)
        drawCircle(color = Color.White, radius = w * 0.098f, center = eyeCenter)
        val pupilOffset = if (isPlayingAudio) 4f else 0f
        drawCircle(color = Color(0xFF0D47A1), radius = w * 0.048f, center = Offset(eyeCenter.x + pupilOffset, eyeCenter.y))
        drawCircle(color = Color.White, radius = w * 0.024f, center = Offset(eyeCenter.x - 3f, eyeCenter.y - 3f))

        // Cute Talking Mouth (Opens and Closes as audio plays or Barnaby speaks)
        val mouthY = h * 0.52f
        val mouthPath = Path().apply {
            moveTo(w * 0.84f, mouthY)
            lineTo(w * 0.91f, mouthY + (mouthHeight * 0.5f))
            lineTo(w * 0.84f, mouthY + (mouthHeight * 1.0f))
            close()
        }
        drawPath(path = mouthPath, color = Color(0xFFD50000))
    }
}

