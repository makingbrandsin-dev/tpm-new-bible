package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.model.AppLanguage
import com.example.ui.audio.AudioPlayerScreen
import com.example.ui.bible.BibleReaderScreen
import com.example.ui.components.AboutAppDialog
import com.example.ui.components.FloatingAudioDock
import com.example.ui.components.LanguageSetupDialog
import com.example.ui.components.SanctuaryBottomNavBar
import com.example.ui.components.SanctuaryDrawerContent
import com.example.ui.components.SanctuaryTopAppBar
import com.example.ui.home.HomeScreen
import com.example.ui.kids.KidsBibleScreen
import com.example.ui.kids.KidsQuizScreen
import com.example.ui.kids.KidsStoryDetailScreen
import com.example.ui.media.MediaScreen
import com.example.ui.memorization.MemorizationScreen
import com.example.ui.theme.SurfaceDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BibleViewModel
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val showLangSetup by viewModel.showLanguageSetupDialog.collectAsState()
    val exoState by viewModel.exoPlaybackState.collectAsState()
    val activeKidsStory by viewModel.activeKidsStory.collectAsState()
    val activeQuizStory by viewModel.activeQuizStory.collectAsState()
    val activeStoryPage by viewModel.activeStoryPage.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val isNotifEnabled = userSettings?.dailyNotificationEnabled == true

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showAboutDialog by remember { mutableStateOf(false) }
    var showFullAudioPlayerSheet by remember { mutableStateOf(false) }

    if (showAboutDialog) {
        AboutAppDialog(onDismiss = { showAboutDialog = false })
    }

    if (showLangSetup) {
        LanguageSetupDialog(
            currentLanguage = currentLang,
            onLanguageSelected = { viewModel.setLanguage(it) },
            onDismiss = { viewModel.dismissLanguageSetupDialog() }
        )
    }

    if (showFullAudioPlayerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFullAudioPlayerSheet = false },
            containerColor = SurfaceDark
        ) {
            AudioPlayerScreen(viewModel = viewModel)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SanctuaryDrawerContent(
                onCloseDrawer = { scope.launch { drawerState.close() } },
                onNavigateTab = { viewModel.setTab(it) },
                onOpenBookmarks = { viewModel.setTab(MainTab.BIBLE) },
                onOpenNotes = { viewModel.setTab(MainTab.BIBLE) },
                onOpenMemorization = { viewModel.setTab(MainTab.MEMORIZATION) },
                onOpenAbout = { showAboutDialog = true },
                onOpenNotifications = { viewModel.setTab(MainTab.HOME) },
                currentLanguage = currentLang,
                onLanguageSelected = { viewModel.setLanguage(it) }
            )
        }
    ) {
        if (activeQuizStory != null) {
            KidsQuizScreen(
                story = activeQuizStory!!,
                viewModel = viewModel,
                onClose = { viewModel.closeStoryQuiz() }
            )
        } else if (activeKidsStory != null) {
            KidsStoryDetailScreen(
                story = activeKidsStory!!,
                currentPageIndex = activeStoryPage,
                viewModel = viewModel,
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
                            MainTab.BIBLE -> "Reader"
                            MainTab.AUDIO -> "Audio Bible"
                            MainTab.KIDS -> "Kids Bible"
                            MainTab.MEDIA -> "Media Bible"
                            MainTab.MEMORIZATION -> "Memorization"
                        },
                        onMenuClick = { scope.launch { drawerState.open() } },
                        currentLanguage = currentLang,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        isNotificationEnabled = isNotifEnabled,
                        onNotificationClick = {
                            if (!isNotifEnabled) {
                                viewModel.setNotificationEnabled(true)
                            }
                            viewModel.setTab(MainTab.HOME)
                        }
                    )
                },
                bottomBar = {
                    Column {
                        // Mini Audio Player docked right above bottom navigation bar
                        FloatingAudioDock(
                            exoState = exoState,
                            onTogglePlayPause = {
                                if (exoState.currentMediaId == null) {
                                    viewModel.playCurrentChapterAudio()
                                } else {
                                    viewModel.toggleAudioPlayPause()
                                }
                            },
                            onOpenFullPlayer = { showFullAudioPlayerSheet = true }
                        )

                        SanctuaryBottomNavBar(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.setTab(it) }
                        )
                    }
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
                }
            }
        }
    }
}
