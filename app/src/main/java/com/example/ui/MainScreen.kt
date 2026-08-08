package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.audio.AudioPlayerScreen
import com.example.ui.bible.BibleReaderScreen
import com.example.ui.components.FloatingAudioDock
import com.example.ui.components.SanctuaryBottomNavBar
import com.example.ui.components.SanctuaryDrawerContent
import com.example.ui.components.SanctuaryTopAppBar
import com.example.ui.home.HomeScreen
import com.example.ui.kids.KidsBibleScreen
import com.example.ui.kids.KidsStoryDetailScreen
import com.example.ui.media.MediaScreen
import com.example.ui.memorization.MemorizationScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: BibleViewModel
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val audioState by viewModel.audioState.collectAsState()
    val activeKidsStory by viewModel.activeKidsStory.collectAsState()
    val activeStoryPage by viewModel.activeStoryPage.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SanctuaryDrawerContent(
                onCloseDrawer = { scope.launch { drawerState.close() } },
                onNavigateTab = { viewModel.setTab(it) },
                onOpenBookmarks = { viewModel.setTab(MainTab.BIBLE) },
                onOpenNotes = { viewModel.setTab(MainTab.BIBLE) },
                onOpenMemorization = { viewModel.setTab(MainTab.MEMORIZATION) }
            )
        }
    ) {
        // If Kids Story Reader is active, show the interactive slide reader
        if (activeKidsStory != null) {
            KidsStoryDetailScreen(
                story = activeKidsStory!!,
                currentPageIndex = activeStoryPage,
                onNextPage = { viewModel.nextStoryPage() },
                onPrevPage = { viewModel.prevStoryPage() },
                onClose = { viewModel.closeKidsStory() }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    SanctuaryTopAppBar(
                        title = when (selectedTab) {
                            MainTab.HOME -> "TPM Bible"
                            MainTab.BIBLE -> "Scripture Reader"
                            MainTab.AUDIO -> "Audio Sanctuary"
                            MainTab.KIDS -> "Kids Bible"
                            MainTab.MEDIA -> "Media Sanctuary"
                            MainTab.MEMORIZATION -> "Memorization"
                        },
                        onMenuClick = { scope.launch { drawerState.open() } },
                        currentLanguage = currentLang,
                        onLanguageSelected = { viewModel.setLanguage(it) }
                    )
                },
                bottomBar = {
                    SanctuaryBottomNavBar(
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.setTab(it) }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .testTag("main_screen_container")
                ) {
                    when (selectedTab) {
                        MainTab.HOME -> HomeScreen(viewModel = viewModel)
                        MainTab.BIBLE -> BibleReaderScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.setTab(MainTab.HOME) }
                        )
                        MainTab.AUDIO -> AudioPlayerScreen(viewModel = viewModel)
                        MainTab.KIDS -> KidsBibleScreen(
                            viewModel = viewModel,
                            onOpenStory = { viewModel.openKidsStory(it) }
                        )
                        MainTab.MEDIA -> MediaScreen(viewModel = viewModel)
                        MainTab.MEMORIZATION -> MemorizationScreen(viewModel = viewModel)
                    }

                    // Floating Audio Dock above Bottom Bar when not in Audio tab
                    if (selectedTab != MainTab.AUDIO && audioState.currentItem != null) {
                        FloatingAudioDock(
                            audioState = audioState,
                            onTogglePlayPause = { viewModel.toggleAudioPlayPause() },
                            onOpenFullPlayer = { viewModel.setTab(MainTab.AUDIO) },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}
