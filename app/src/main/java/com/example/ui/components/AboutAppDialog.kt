package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .testTag("about_app_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Logo Icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(CelestialGold)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "TPM Bible Logo",
                        tint = OnPrimaryDark,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // App Title & Version
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TPM Bible",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = CelestialGold
                    )

                    Surface(
                        color = CelestialGold.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Version 1.0.0 (Build 100)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialGold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "The Pentecostal Mission",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurfaceVariantMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                HorizontalDivider(color = MutedOutlineVariant)

                // Description
                Text(
                    text = "TPM Bible is a comprehensive multi-language Holy Bible application created for spiritual edification, daily scriptures, and prayer life. Featuring Holy Bible in English, Tamil, and Telugu, along with Kids Bible Adventures, Audio Bible Streaming, and Scripture Memorization.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = OnSurfaceLight,
                    textAlign = TextAlign.Center
                )

                // Key Features List
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureItem(
                        icon = Icons.Default.Language,
                        title = "Trilingual Bible",
                        desc = "English KJV, Tamil (பரிசுத்த வேதாகமம்), Telugu (పరిశుద్ధ గ్రంథము)"
                    )
                    FeatureItem(
                        icon = Icons.Default.Headset,
                        title = "Audio Bible & Sermons",
                        desc = "Background audio playback powered by Media3 ExoPlayer"
                    )
                    FeatureItem(
                        icon = Icons.Default.ChildCare,
                        title = "Kids Bible Stories",
                        desc = "Animated storybooks with Teacher Grace & interactive quizzes"
                    )
                    FeatureItem(
                        icon = Icons.Default.MilitaryTech,
                        title = "Scripture Memorization",
                        desc = "Interactive flashcards, streak counters & memory tests"
                    )
                    FeatureItem(
                        icon = Icons.Default.NotificationsActive,
                        title = "Daily Verse Reminders",
                        desc = "Custom notification scheduling & daily verse alerts"
                    )
                }

                HorizontalDivider(color = MutedOutlineVariant)

                // Dedicated Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CelestialGold.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, CelestialGold.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CelestialGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = OnPrimaryDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "This app is developed by Making Brands for TPM Bollarum children's for them to learn Gods words.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceLight,
                            lineHeight = 18.sp
                        )
                    }
                }

                HorizontalDivider(color = MutedOutlineVariant)

                // Footer & Copyright
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "© 2026 TPM Bible Ministry",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceVariantMuted
                    )
                    Text(
                        text = "All rights reserved • Made with faith & devotion",
                        fontSize = 11.sp,
                        color = OnSurfaceVariantMuted.copy(alpha = 0.8f)
                    )
                }

                // Close Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CelestialGold),
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("close_about_dialog_button")
                ) {
                    Text(
                        text = "Close",
                        fontWeight = FontWeight.Bold,
                        color = OnPrimaryDark
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceDark)
                .border(1.dp, CelestialGold.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CelestialGold,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceLight
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = OnSurfaceVariantMuted
            )
        }
    }
}
