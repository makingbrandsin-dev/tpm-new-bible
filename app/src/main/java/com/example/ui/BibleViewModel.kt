package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BibleDatabase
import com.example.data.BibleRepository
import com.example.data.FlashcardEntity
import com.example.data.UserSettingsEntity
import com.example.data.VerseEntity
import com.example.model.AppLanguage
import com.example.model.KidsStory
import com.example.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    BIBLE,
    AUDIO,
    KIDS,
    MEDIA,
    MEMORIZATION
}

data class AudioState(
    val currentItem: MediaItem? = null,
    val isPlaying: Boolean = false,
    val progress: Float = 0.35f,
    val currentTimeStr: String = "01:24",
    val totalTimeStr: String = "04:32"
)

class BibleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = BibleDatabase.getDatabase(application)
    val repository = BibleRepository(db.bibleDao())

    private val _selectedTab = MutableStateFlow(MainTab.HOME)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _selectedBook = MutableStateFlow("John")
    val selectedBook: StateFlow<String> = _selectedBook.asStateFlow()

    private val _selectedChapter = MutableStateFlow(1)
    val selectedChapter: StateFlow<Int> = _selectedChapter.asStateFlow()

    private val _selectedVerseId = MutableStateFlow<String?>("John_1_3")
    val selectedVerseId: StateFlow<String?> = _selectedVerseId.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(20)
    val fontSizeSp: StateFlow<Int> = _fontSizeSp.asStateFlow()

    private val _isSerifFont = MutableStateFlow(true)
    val isSerifFont: StateFlow<Boolean> = _isSerifFont.asStateFlow()

    private val _audioState = MutableStateFlow(
        AudioState(
            currentItem = repository.getSampleMedia().first(),
            isPlaying = false
        )
    )
    val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

    // Kids Story State
    private val _activeKidsStory = MutableStateFlow<KidsStory?>(null)
    val activeKidsStory: StateFlow<KidsStory?> = _activeKidsStory.asStateFlow()

    private val _activeStoryPage = MutableStateFlow(1)
    val activeStoryPage: StateFlow<Int> = _activeStoryPage.asStateFlow()

    // Drawer State
    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    // Verse List from Room
    val currentVerses: StateFlow<List<VerseEntity>> = repository.getVerses("John", 1)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bookmarkedVerses: StateFlow<List<VerseEntity>> = repository.getBookmarkedVerses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val flashcards: StateFlow<List<FlashcardEntity>> = repository.getAllFlashcards()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userSettings: StateFlow<UserSettingsEntity?> = repository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettingsEntity()
        )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun setTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun selectVerse(id: String?) {
        _selectedVerseId.value = id
    }

    fun toggleBookmark(verse: VerseEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(verse)
        }
    }

    fun toggleAudioPlayPause() {
        _audioState.value = _audioState.value.copy(isPlaying = !_audioState.value.isPlaying)
    }

    fun playMediaItem(item: MediaItem) {
        _audioState.value = AudioState(
            currentItem = item,
            isPlaying = true,
            progress = 0.1f,
            currentTimeStr = "00:15",
            totalTimeStr = item.duration
        )
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = userSettings.value ?: UserSettingsEntity()
            repository.saveSettings(current.copy(dailyNotificationEnabled = enabled))
        }
    }

    fun setNotificationTime(timeStr: String) {
        viewModelScope.launch {
            val current = userSettings.value ?: UserSettingsEntity()
            repository.saveSettings(current.copy(notificationTime = timeStr))
        }
    }

    fun setFontSize(sizeSp: Int) {
        _fontSizeSp.value = sizeSp.coerceIn(14, 28)
    }

    fun toggleSerifFont() {
        _isSerifFont.value = !_isSerifFont.value
    }

    fun openKidsStory(story: KidsStory) {
        _activeKidsStory.value = story
        _activeStoryPage.value = 1
    }

    fun closeKidsStory() {
        _activeKidsStory.value = null
    }

    fun nextStoryPage() {
        val story = _activeKidsStory.value ?: return
        if (_activeStoryPage.value < story.pages.size) {
            _activeStoryPage.value += 1
        }
    }

    fun prevStoryPage() {
        if (_activeStoryPage.value > 1) {
            _activeStoryPage.value -= 1
        }
    }

    fun markFlashcardMastered(flashcard: FlashcardEntity) {
        viewModelScope.launch {
            repository.updateFlashcard(flashcard.copy(isMastered = !flashcard.isMastered))
        }
    }

    fun addCustomFlashcard(ref: String, en: String, ta: String, te: String) {
        viewModelScope.launch {
            repository.insertFlashcard(
                FlashcardEntity(
                    reference = ref,
                    textEnglish = en,
                    textTamil = ta,
                    textTelugu = te,
                    isMastered = false,
                    streakDays = 1
                )
            )
        }
    }

    fun toggleDrawer(open: Boolean? = null) {
        _isDrawerOpen.value = open ?: !_isDrawerOpen.value
    }

    fun changeChapter(delta: Int) {
        val newChap = (_selectedChapter.value + delta).coerceIn(1, 21)
        _selectedChapter.value = newChap
    }
}
