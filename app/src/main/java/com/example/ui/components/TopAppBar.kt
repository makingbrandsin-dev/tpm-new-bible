package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import android.widget.Toast
import com.example.model.AppLanguage
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainerHigh

@Composable
fun SanctuaryTopAppBar(
    title: String = "TPM Bible",
    onMenuClick: () -> Unit,
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    isNotificationEnabled: Boolean = false,
    onNotificationClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 12.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.testTag("app_bar_menu_button")
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = CelestialGold
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            AnimatedContent(
                targetState = title,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "TitleAnimation"
            ) { targetTitle ->
                Text(
                    text = targetTitle,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = CelestialGold,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .testTag("app_bar_title")
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Notification Bell Button with Active Indicator State
            IconButton(
                onClick = {
                    onNotificationClick()
                    if (isNotificationEnabled) {
                        Toast.makeText(context, "Daily Notifications are Active 🔔", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Daily Notifications Enabled! 🔔", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.testTag("top_app_bar_bell_button")
            ) {
                if (isNotificationEnabled) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = CelestialGold,
                                contentColor = OnPrimaryDark,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CelestialGold.copy(alpha = 0.2f))
                                .padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Daily Notification Active",
                                tint = CelestialGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Daily Notification Inactive",
                        tint = OnSurfaceVariantMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Animated Language Switcher Bar on Top
            AnimatedLanguageSelectorBar(
                currentLanguage = currentLanguage,
                onLanguageSelected = onLanguageSelected
            )
        }
    }
}

@Composable
private fun AnimatedLanguageSelectorBar(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(SurfaceContainerHigh)
            .padding(3.dp)
            .testTag("animated_language_selector_bar")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppLanguage.entries.forEach { lang ->
                val isSelected = currentLanguage == lang

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) CelestialGold else Color.Transparent,
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                    label = "LangBgColor"
                )

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) OnPrimaryDark else OnSurfaceVariantMuted,
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                    label = "LangTextColor"
                )

                val shortLabel = when (lang) {
                    AppLanguage.ENGLISH -> "ENG"
                    AppLanguage.TAMIL -> "TAM"
                    AppLanguage.TELUGU -> "TEL"
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .clickable { onLanguageSelected(lang) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = shortLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }
        }
    }
}

