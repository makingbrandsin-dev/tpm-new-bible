package com.example.ui.home

import android.app.TimePickerDialog
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.notification.NotificationHelper
import com.example.ui.BibleViewModel
import com.example.ui.MainTab
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.MutedOutlineVariant
import com.example.ui.theme.OnPrimaryContainerDark
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.PrimaryContainerGold
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceDark
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()

    val isNotifEnabled = userSettings?.dailyNotificationEnabled ?: true
    val notifTime = userSettings?.notificationTime ?: "07:00"

    var dailyVerseLang by remember { mutableStateOf(currentLang) }

    LaunchedEffect(currentLang) {
        dailyVerseLang = currentLang
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("home_screen_content"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Verse of the Day Card (Tap card or language chips to change language automatically)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val nextLang = when (dailyVerseLang) {
                        AppLanguage.ENGLISH -> AppLanguage.TAMIL
                        AppLanguage.TAMIL -> AppLanguage.TELUGU
                        AppLanguage.TELUGU -> AppLanguage.ENGLISH
                    }
                    dailyVerseLang = nextLang
                    viewModel.setLanguage(nextLang)
                }
                .testTag("verse_of_the_day_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryContainerGold),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VERSE OF THE DAY",
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimaryContainerDark.copy(alpha = 0.9f)
                    )

                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "Quote",
                        tint = OnPrimaryContainerDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Animated Single Language Daily Verse
                AnimatedContent(
                    targetState = dailyVerseLang,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(280)) { width -> width } + fadeIn(animationSpec = tween(280)) togetherWith
                                slideOutHorizontally(animationSpec = tween(280)) { width -> -width } + fadeOut(animationSpec = tween(280))
                    },
                    label = "DailyVerseLangAnim"
                ) { targetLang ->
                    val (verseText, langLabel) = when (targetLang) {
                        AppLanguage.ENGLISH -> "\"The Lord is my shepherd; I shall not want.\"" to "English (KJV) • Psalm 23:1"
                        AppLanguage.TAMIL -> "\"கர்த்தர் என் மேய்ப்பராயிருக்கிறார்; நான் தாழ்ச்சியடையேன்.\"" to "Tamil • சங்கீதம் 23:1"
                        AppLanguage.TELUGU -> "\"యెహోవా నా కాపరి, నాకు లేమి కలుగదు.\"" to "Telugu • కీర్తనలు 23:1"
                    }

                    Column {
                        Text(
                            text = verseText,
                            fontFamily = FontFamily.Serif,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 30.sp,
                            color = OnPrimaryContainerDark
                        )
                        Text(
                            text = langLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnPrimaryContainerDark.copy(alpha = 0.85f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                val context = LocalContext.current
                val (verseTextForShare, langLabelForShare) = when (dailyVerseLang) {
                    AppLanguage.ENGLISH -> "\"The Lord is my shepherd; I shall not want.\"" to "Psalm 23:1 (KJV)"
                    AppLanguage.TAMIL -> "\"கர்த்தர் என் மேய்ப்பராயிருக்கிறார்; நான் தாழ்ச்சியடையேன்.\"" to "சங்கீதம் 23:1"
                    AppLanguage.TELUGU -> "\"యెహోవా నా కాపరి, నాకు లేమి కలుగదు.\"" to "కీర్తనలు 23:1"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            viewModel.setTab(MainTab.BIBLE)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceDark,
                            contentColor = CelestialGold
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("read_chapter_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Read Chapter",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Read",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // General Share Button
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, "✝️ Verse of the Day\n\n$verseTextForShare\n\n— $langLabelForShare\n\nShared via TPM Bible App")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Verse of the Day"))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OnPrimaryContainerDark.copy(alpha = 0.18f),
                            contentColor = OnPrimaryContainerDark
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("share_verse_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Verse",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // WhatsApp Share Button
                    Button(
                        onClick = {
                            val message = "✝️ Verse of the Day\n\n$verseTextForShare\n\n— $langLabelForShare\n\nShared via TPM Bible App"
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
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF25D366),
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("whatsapp_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Share on WhatsApp",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WhatsApp",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Quick Access Grid
        // 1. Bible Reading (Smaller, compact card with text beside icon)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.setTab(MainTab.BIBLE) }
                .testTag("card_bible_reading"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF38321E))
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = CelestialGold,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Bible Reading",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight
                    )
                    Text(
                        text = "Continue reading • John 1",
                        fontSize = 12.sp,
                        color = OnSurfaceVariantMuted
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Go",
                    tint = OnSurfaceVariantMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 5. Daily Notifications & Time Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("notifications_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isNotifEnabled) CelestialGold.copy(alpha = 0.25f) else Color(0xFF38321E))
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = if (isNotifEnabled) Icons.Default.NotificationsActive else Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = if (isNotifEnabled) CelestialGold else OnSurfaceVariantMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Daily Notifications",
                                fontFamily = FontFamily.Serif,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceLight
                            )

                            Text(
                                text = if (isNotifEnabled) "Bell option active in top bar 🔔" else "Receive daily scripture reminders",
                                fontSize = 11.sp,
                                color = if (isNotifEnabled) CelestialGold else OnSurfaceVariantMuted
                            )
                        }
                    }

                    Switch(
                        checked = isNotifEnabled,
                        onCheckedChange = { viewModel.setNotificationEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = CelestialGold,
                            uncheckedThumbColor = OnSurfaceVariantMuted,
                            uncheckedTrackColor = SurfaceDark
                        ),
                        modifier = Modifier.testTag("notification_switch")
                    )
                }

                HorizontalDivider(color = MutedOutlineVariant.copy(alpha = 0.3f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notification Time",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurfaceVariantMuted
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceDark)
                            .clickable {
                                val cal = Calendar.getInstance()
                                val dialog = TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        val formatted = String.format("%02d:%02d AM", if (hourOfDay % 12 == 0) 12 else hourOfDay % 12, minute)
                                        viewModel.setNotificationTime(formatted)
                                    },
                                    cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE),
                                    false
                                )
                                dialog.show()
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = notifTime,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialGold
                        )
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Select Time",
                            tint = CelestialGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (isNotifEnabled) {
                    Button(
                        onClick = {
                            NotificationHelper.showDailyVerseNotification(
                                context = context,
                                title = "TPM Bible • Daily Verse",
                                verseText = "\"The Lord is my shepherd; I shall not want.\" — Psalm 23:1"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceDark,
                            contentColor = CelestialGold
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .testTag("send_test_notification_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Send Test Daily Notification",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 6. AI Bible Verse Assistant Section
        AiVerseAssistantSection(viewModel = viewModel)

        Spacer(modifier = Modifier.height(24.dp))
    }
}
