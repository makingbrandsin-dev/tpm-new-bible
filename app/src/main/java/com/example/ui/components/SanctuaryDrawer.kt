package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.MainTab
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.MutedOutlineVariant
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceLight
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow

@Composable
fun SanctuaryDrawerContent(
    onCloseDrawer: () -> Unit,
    onNavigateTab: (MainTab) -> Unit,
    onOpenBookmarks: () -> Unit,
    onOpenNotes: () -> Unit,
    onOpenMemorization: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenNotifications: () -> Unit,
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onLanguageSelected: (AppLanguage) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(SurfaceContainerLow)
            .statusBarsPadding()
            .padding(24.dp)
            .testTag("sanctuary_drawer")
    ) {
        // Top 20% empty spacing as requested
        Spacer(modifier = Modifier.fillMaxHeight(0.18f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "TPM Bible",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = CelestialGold
                )
                Text(
                    text = "The Pentecostal Mission",
                    fontSize = 12.sp,
                    color = OnSurfaceVariantMuted
                )
            }

            IconButton(onClick = onCloseDrawer) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Drawer",
                    tint = OnSurfaceVariantMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        DrawerMenuItem(
            icon = Icons.Default.MilitaryTech,
            label = "Memorization Dashboard",
            onClick = {
                onOpenMemorization()
                onCloseDrawer()
            }
        )

        DrawerMenuItem(
            icon = Icons.Default.Bookmark,
            label = "Bookmarks",
            onClick = {
                onOpenBookmarks()
                onCloseDrawer()
            }
        )

        DrawerMenuItem(
            icon = Icons.Default.EditNote,
            label = "My Notes & Journal",
            onClick = {
                onOpenNotes()
                onCloseDrawer()
            }
        )

        DrawerMenuItem(
            icon = Icons.Default.NotificationsActive,
            label = "Daily Notifications",
            onClick = {
                onOpenNotifications()
                onCloseDrawer()
            }
        )

        DrawerMenuItem(
            icon = Icons.Default.Info,
            label = "About TPM Bible",
            onClick = {
                onOpenAbout()
                onCloseDrawer()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Default Language Selection Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceContainerHigh)
                .padding(12.dp)
                .testTag("drawer_default_language_card")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Default Language",
                    tint = CelestialGold,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Default App Language",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceLight
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    AppLanguage.ENGLISH to "English",
                    AppLanguage.TAMIL to "தமிழ்",
                    AppLanguage.TELUGU to "తెలుగు"
                ).forEach { (lang, label) ->
                    val isSel = currentLanguage == lang
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) CelestialGold else SurfaceContainerLow)
                            .clickable { onLanguageSelected(lang) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) OnPrimaryDark else OnSurfaceVariantMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(color = MutedOutlineVariant)

        Spacer(modifier = Modifier.height(16.dp))

        // Version Info Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TPM Bible",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = CelestialGold
            )
            Text(
                text = "Version 1.0.0 (Build 100)",
                fontSize = 11.sp,
                color = OnSurfaceVariantMuted
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = CelestialGold,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = OnSurfaceLight
        )
    }
}
