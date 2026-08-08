package com.example.ui.kids

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.KidsStory
import com.example.ui.BibleViewModel
import com.example.ui.theme.*

@Composable
fun KidsQuizScreen(
    story: KidsStory,
    viewModel: BibleViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val questions = story.quizQuestions

    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedOptionIdx by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableStateOf(0) }
    var isExamFinished by remember { mutableStateOf(false) }

    val activeQuestion = questions.getOrNull(currentQuestionIdx)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .statusBarsPadding()
            .padding(top = 12.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .testTag("kids_quiz_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
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
                text = "${story.title(currentLang)} • Teacher's Exam",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CelestialGold
            )

            // Language Switcher
            IconButton(onClick = {
                val next = when (currentLang) {
                    AppLanguage.ENGLISH -> AppLanguage.TAMIL
                    AppLanguage.TAMIL -> AppLanguage.TELUGU
                    AppLanguage.TELUGU -> AppLanguage.ENGLISH
                }
                viewModel.setLanguage(next)
            }) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Switch Language",
                    tint = CelestialGold
                )
            }
        }

        if (questions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No questions available for this story yet!",
                    color = OnSurfaceLight
                )
            }
        } else if (!isExamFinished && activeQuestion != null) {
            // Interactive Exam Mode
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Exam Progress Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${currentQuestionIdx + 1} of ${questions.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelestialGold
                    )

                    Surface(
                        color = CelestialGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Language: ${currentLang.displayName}",
                            fontSize = 12.sp,
                            color = CelestialGold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { (currentQuestionIdx + 1).toFloat() / questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = CelestialGold,
                    trackColor = SurfaceContainerHigh
                )

                // Teacher Grace Presentation Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(KidsPink)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Teacher Grace",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Teacher Grace asks:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = KidsPink
                            )
                            Text(
                                text = activeQuestion.question(currentLang),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceLight,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                // Options List
                val options = activeQuestion.options(currentLang)
                options.forEachIndexed { index, optionText ->
                    val isSelected = selectedOptionIdx == index
                    val isCorrectOption = activeQuestion.correctOptionIndex == index

                    val cardBg = when {
                        isAnswerSubmitted && isCorrectOption -> Color(0xFF2E7D32)
                        isAnswerSubmitted && isSelected && !isCorrectOption -> Color(0xFFC62828)
                        isSelected -> CelestialGold
                        else -> SurfaceContainer
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.5.dp,
                                if (isSelected) CelestialGold else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable(enabled = !isAnswerSubmitted) {
                                selectedOptionIdx = index
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${('A' + index)}. $optionText",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isAnswerSubmitted || isSelected) Color.White else OnSurfaceLight,
                                modifier = Modifier.weight(1f)
                            )

                            if (isAnswerSubmitted && isCorrectOption) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                // Teacher's Explanation Banner after answering
                AnimatedVisibility(visible = isAnswerSubmitted) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = CelestialGold
                            )
                            Text(
                                text = activeQuestion.teacherExplanation(currentLang),
                                fontSize = 14.sp,
                                color = Color.White,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Button: Submit Answer or Next Question
                if (!isAnswerSubmitted) {
                    Button(
                        onClick = {
                            if (selectedOptionIdx != null) {
                                isAnswerSubmitted = true
                                if (selectedOptionIdx == activeQuestion.correctOptionIndex) {
                                    correctAnswersCount += 1
                                }
                            }
                        },
                        enabled = selectedOptionIdx != null,
                        colors = ButtonDefaults.buttonColors(containerColor = CelestialGold),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_answer_button")
                    ) {
                        Text(
                            text = "Submit Answer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnPrimaryDark
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentQuestionIdx + 1 < questions.size) {
                                currentQuestionIdx += 1
                                selectedOptionIdx = null
                                isAnswerSubmitted = false
                            } else {
                                isExamFinished = true
                                viewModel.submitQuizResult(
                                    storyId = story.id,
                                    correctCount = correctAnswersCount,
                                    totalCount = questions.size
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KidsGreen),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("next_question_button")
                    ) {
                        Text(
                            text = if (currentQuestionIdx + 1 < questions.size) "Next Question ➔" else "See My Final Score & Marks 🎉",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            // Final Marks & Teacher's Report Card Screen!
            val totalQ = questions.size
            val percentage = if (totalQ > 0) ((correctAnswersCount.toFloat() / totalQ) * 100).toInt() else 100
            val marks = percentage

            val gradeStr = when {
                percentage >= 90 -> "A+ Exceptional!"
                percentage >= 70 -> "A Excellent!"
                percentage >= 50 -> "B Good Effort!"
                else -> "C Keep Practicing!"
            }

            val teacherRemarks = when (currentLang) {
                AppLanguage.ENGLISH -> when {
                    percentage >= 90 -> "Outstanding! You listened so carefully to God's Word today, my star student! ⭐"
                    percentage >= 70 -> "Great job! You answered most questions correctly. Keep learning!"
                    else -> "Good effort! Read the story once more with Teacher Grace to get 100 Marks!"
                }
                AppLanguage.TAMIL -> when {
                    percentage >= 90 -> "அற்புதம்! தேவனுடைய வார்த்தையை மிகக் கவனமாகக் கற்றுக்கொண்டாய், என் அன்புக் குழந்தையே! ⭐"
                    percentage >= 70 -> "நன்றாகச் செய்தாய்! வேதக் கதையை அழகாகப் புரிந்துகொண்டாய்."
                    else -> "நல்ல முயற்சி! 100 மதிப்பெண்கள் பெற கதையை மீண்டும் ஒருமுறை வாசி!"
                }
                AppLanguage.TELUGU -> when {
                    percentage >= 90 -> "అద్భుతం! దేవుని వాక్యాన్ని చాలా శ్రద్ధగా నేర్చుకున్నావు, నా ప్రియమైన విద్యార్థీ! ⭐"
                    percentage >= 70 -> "చాలా బాగుంది! కథను బాగా అర్థం చేసుకున్నావు."
                    else -> "మంచి ప్రయత్నం! 100 మార్కులు రావడానికి కథను మళ్లీ చదువు!"
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Certificate Header Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = CelestialGold,
                            modifier = Modifier.size(64.dp)
                        )

                        Text(
                            text = "Teacher's Score & Report Card",
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialGold
                        )

                        // Stars Row
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(3) { starIdx ->
                                val activeStar = when {
                                    percentage >= 90 -> true
                                    percentage >= 70 -> starIdx < 2
                                    else -> starIdx < 1
                                }
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (activeStar) CelestialGold else OnSurfaceVariantMuted,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        // Marks Big Badge
                        Surface(
                            color = CelestialGold,
                            shape = RoundedCornerShape(20.dp),
                            shadowElevation = 6.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$marks / 100 Marks",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnPrimaryDark
                                )
                                Text(
                                    text = "Grade: $gradeStr",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnPrimaryDark.copy(alpha = 0.9f)
                                )
                            }
                        }

                        HorizontalDivider(color = MutedOutlineVariant)

                        // Teacher Grace Feedback Box
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(KidsPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = "Teacher Grace says:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidsPink
                                )
                                Text(
                                    text = teacherRemarks,
                                    fontSize = 14.sp,
                                    color = OnSurfaceLight,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            currentQuestionIdx = 0
                            selectedOptionIdx = null
                            isAnswerSubmitted = false
                            correctAnswersCount = 0
                            isExamFinished = false
                        },
                        shape = CircleShape,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Retake Exam", color = CelestialGold)
                    }

                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(containerColor = CelestialGold),
                        shape = CircleShape,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Done", color = OnPrimaryDark, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
