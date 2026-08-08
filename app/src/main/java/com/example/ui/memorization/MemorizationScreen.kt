package com.example.ui.memorization

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FlashcardEntity
import com.example.model.AppLanguage
import com.example.ui.BibleViewModel
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.MutedOutlineVariant
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow

@Composable
fun MemorizationScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val flashcards by viewModel.flashcards.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    val flippedStates = remember { mutableStateMapOf<Int, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("memorization_dashboard"),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Dashboard Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Memorization Dashboard",
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceLight
            )
            Text(
                text = "Thy word have I hid in mine heart.",
                fontSize = 14.sp,
                color = OnSurfaceVariantMuted,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Circular Progress Ring Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(190.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 16.dp.toPx()
                    // Track circle
                    drawCircle(
                        color = SurfaceContainerHigh,
                        style = Stroke(width = strokeWidth)
                    )
                    // 75% arc
                    drawArc(
                        color = CelestialGold,
                        startAngle = -90f,
                        sweepAngle = 270f, // 75% of 360
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "75%",
                        fontFamily = FontFamily.Serif,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestialGold
                    )
                    Text(
                        text = "Weekly Goal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceVariantMuted
                    )
                }
            }

            // Quick Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .border(1.dp, MutedOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = CelestialGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Mastered",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted
                            )
                            Text(
                                text = "12 Verses",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceLight
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .border(1.dp, MutedOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = CelestialGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Streak",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted
                            )
                            Text(
                                text = "5 Days",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceLight
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = MutedOutlineVariant.copy(alpha = 0.3f))

        // Your Flashcards Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Flashcards",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceLight
            )

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = CelestialGold,
                contentColor = OnPrimaryDark,
                modifier = Modifier
                    .size(42.dp)
                    .testTag("add_flashcard_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Flashcard")
            }
        }

        // Flashcards Grid / List
        flashcards.forEach { card ->
            val isFlipped = flippedStates[card.id] ?: false

            val rotation by animateFloatAsState(
                targetValue = if (isFlipped) 180f else 0f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                label = "flipRotation"
            )

            val textToShow = when (currentLang) {
                AppLanguage.ENGLISH -> card.textEnglish
                AppLanguage.TAMIL -> card.textTamil
                AppLanguage.TELUGU -> card.textTelugu
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12 * density
                    }
                    .clickable {
                        flippedStates[card.id] = !isFlipped
                    }
                    .testTag("flashcard_${card.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (card.isMastered) SurfaceContainer.copy(alpha = 0.9f) else SurfaceContainer
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (card.isMastered) 2.dp else 1.dp,
                    color = if (card.isMastered) CelestialGold else MutedOutlineVariant.copy(alpha = 0.4f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    if (rotation <= 90f) {
                        // FRONT SIDE
                        Box(modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = CelestialGold.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.TopStart)
                            )

                            if (card.isMastered) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .clip(CircleShape)
                                        .background(CelestialGold.copy(alpha = 0.2f))
                                        .padding(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Stars,
                                        contentDescription = "Mastered",
                                        tint = CelestialGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = card.reference,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (card.isMastered) OnSurfaceVariantMuted else CelestialGold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                if (card.isMastered) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Mastered",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CelestialGold
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = CelestialGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .border(1.dp, CelestialGold, CircleShape)
                                            .padding(horizontal = 16.dp, vertical = 6.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "Flip to Read",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CelestialGold
                                            )
                                            Icon(
                                                imageVector = Icons.Default.Autorenew,
                                                contentDescription = null,
                                                tint = CelestialGold,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // BACK SIDE
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f }
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = textToShow,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = OnSurfaceLight,
                                    lineHeight = 26.sp
                                )
                            }

                            Row(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = {
                                    viewModel.markFlashcardMastered(card)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Mastered",
                                        tint = CelestialGold
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

    // Add New Flashcard Dialog
    if (showAddDialog) {
        var refInput by remember { mutableStateOf("") }
        var enInput by remember { mutableStateOf("") }
        var taInput by remember { mutableStateOf("") }
        var teInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Add Verse Flashcard",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    color = CelestialGold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = refInput,
                        onValueChange = { refInput = it },
                        label = { Text("Verse Reference (e.g. Genesis 1:1)") }
                    )
                    OutlinedTextField(
                        value = enInput,
                        onValueChange = { enInput = it },
                        label = { Text("English Text") }
                    )
                    OutlinedTextField(
                        value = taInput,
                        onValueChange = { taInput = it },
                        label = { Text("Tamil Text (Optional)") }
                    )
                    OutlinedTextField(
                        value = teInput,
                        onValueChange = { teInput = it },
                        label = { Text("Telugu Text (Optional)") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (refInput.isNotBlank() && enInput.isNotBlank()) {
                            viewModel.addCustomFlashcard(
                                ref = refInput,
                                en = enInput,
                                ta = if (taInput.isBlank()) enInput else taInput,
                                te = if (teInput.isBlank()) enInput else teInput
                            )
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CelestialGold)
                ) {
                    Text("Save", color = OnPrimaryDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = OnSurfaceVariantMuted)
                }
            }
        )
    }
}
