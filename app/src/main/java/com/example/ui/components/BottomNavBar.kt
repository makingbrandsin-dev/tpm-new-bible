package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainTab
import com.example.ui.theme.CelestialGold
import com.example.ui.theme.OnPrimaryDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainer

data class NavItemData(
    val tab: MainTab,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun SanctuaryBottomNavBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItemData(MainTab.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavItemData(MainTab.BIBLE, "Reader", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        NavItemData(MainTab.AUDIO, "Audio", Icons.Filled.Headset, Icons.Outlined.Headset),
        NavItemData(MainTab.KIDS, "Kids", Icons.Filled.ChildCare, Icons.Outlined.ChildCare),
        NavItemData(MainTab.MEDIA, "Media", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircle)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = SurfaceContainer,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selectedTab == item.tab

                val bgAnim by animateColorAsState(
                    targetValue = if (isSelected) CelestialGold else MaterialTheme.colorScheme.surfaceContainer,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "bgAnim"
                )

                val iconTintAnim by animateColorAsState(
                    targetValue = if (isSelected) OnPrimaryDark else OnSurfaceVariantMuted,
                    label = "iconTintAnim"
                )

                val textColorAnim by animateColorAsState(
                    targetValue = if (isSelected) CelestialGold else OnSurfaceVariantMuted,
                    label = "textColorAnim"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onTabSelected(item.tab)
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("nav_item_${item.title.lowercase()}")
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(bgAnim)
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = iconTintAnim,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColorAnim
                    )
                }
            }
        }
    }
}
