package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioPlayerManager
import com.example.audio.ExoPlaybackState
import com.example.data.BibleCatalog
import com.example.data.BibleDatabase
import com.example.data.BibleRepository
import com.example.data.FlashcardEntity
import com.example.data.UserSettingsEntity
import com.example.data.VerseEntity
import com.example.model.AppLanguage
import com.example.model.KidsStory
import com.example.model.MediaItem
import com.example.model.StoryQuizResult
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

class BibleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = BibleDatabase.getDatabase(application)
    val repository = BibleRepository(db.bibleDao())

    // Jetpack Media3 ExoPlayer Audio Player Manager
    val audioPlayerManager = AudioPlayerManager(application)
    val exoPlaybackState: StateFlow<ExoPlaybackState> = audioPlayerManager.playbackState

    private val _selectedTab = MutableStateFlow(MainTab.HOME)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _showLanguageSetupDialog = MutableStateFlow(false)
    val showLanguageSetupDialog: StateFlow<Boolean> = _showLanguageSetupDialog.asStateFlow()

    private val _selectedBook = MutableStateFlow("John")
    val selectedBook: StateFlow<String> = _selectedBook.asStateFlow()

    private val _selectedChapter = MutableStateFlow(1)
    val selectedChapter: StateFlow<Int> = _selectedChapter.asStateFlow()

    private val _selectedVerseId = MutableStateFlow<String?>("John_1_1")
    val selectedVerseId: StateFlow<String?> = _selectedVerseId.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(20)
    val fontSizeSp: StateFlow<Int> = _fontSizeSp.asStateFlow()

    private val _isSerifFont = MutableStateFlow(true)
    val isSerifFont: StateFlow<Boolean> = _isSerifFont.asStateFlow()

    // Kids Story State
    private val _activeKidsStory = MutableStateFlow<KidsStory?>(null)
    val activeKidsStory: StateFlow<KidsStory?> = _activeKidsStory.asStateFlow()

    private val _activeStoryPage = MutableStateFlow(1)
    val activeStoryPage: StateFlow<Int> = _activeStoryPage.asStateFlow()

    // Quiz & Exam State
    private val _activeQuizStory = MutableStateFlow<KidsStory?>(null)
    val activeQuizStory: StateFlow<KidsStory?> = _activeQuizStory.asStateFlow()

    private val _quizScores = MutableStateFlow<Map<String, StoryQuizResult>>(
        mapOf(
            "noahs_ark" to StoryQuizResult(
                storyId = "noahs_ark",
                totalQuestions = 2,
                correctAnswers = 2,
                scoreMarks = 100,
                grade = "A+",
                starsEarned = 3
            )
        )
    )
    val quizScores: StateFlow<Map<String, StoryQuizResult>> = _quizScores.asStateFlow()

    // Drawer State
    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    // Dynamic Verses from BibleCatalog for selected book & chapter combined with database bookmark overrides
    val currentVerses: StateFlow<List<VerseEntity>> = combine(
        _selectedBook,
        _selectedChapter,
        repository.getBookmarkedVerses()
    ) { book, chapter, bookmarks ->
        val generated = BibleCatalog.generateVersesForChapter(book, chapter)
        val bookmarkIds = bookmarks.map { it.id }.toSet()
        generated.map { v ->
            if (bookmarkIds.contains(v.id)) {
                v.copy(isBookmarked = true)
            } else {
                v
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BibleCatalog.generateVersesForChapter("John", 1)
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
        val prefs = application.getSharedPreferences("tpm_bible_prefs", android.content.Context.MODE_PRIVATE)
        val isLanguageSet = prefs.getBoolean("is_default_language_set", false)
        if (!isLanguageSet) {
            _showLanguageSetupDialog.value = true
        } else {
            val savedLang = prefs.getString("default_language", AppLanguage.ENGLISH.name)
            _currentLanguage.value = try { AppLanguage.valueOf(savedLang ?: "ENGLISH") } catch (e: Exception) { AppLanguage.ENGLISH }
        }

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun setTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        val prefs = getApplication<Application>().getSharedPreferences("tpm_bible_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("default_language", language.name).putBoolean("is_default_language_set", true).apply()
    }

    fun dismissLanguageSetupDialog() {
        _showLanguageSetupDialog.value = false
    }

    fun openLanguageSetupDialog() {
        _showLanguageSetupDialog.value = true
    }

    fun selectBook(bookName: String) {
        _selectedBook.value = bookName
        _selectedChapter.value = 1
        _selectedVerseId.value = null
    }

    fun selectChapter(chapterNum: Int) {
        val maxChaps = BibleCatalog.findBook(_selectedBook.value).totalChapters
        _selectedChapter.value = chapterNum.coerceIn(1, maxChaps)
        _selectedVerseId.value = null
    }

    fun changeChapter(delta: Int) {
        val maxChaps = BibleCatalog.findBook(_selectedBook.value).totalChapters
        val newChap = (_selectedChapter.value + delta).coerceIn(1, maxChaps)
        _selectedChapter.value = newChap
    }

    fun selectVerse(id: String?) {
        _selectedVerseId.value = id
    }

    fun toggleBookmark(verse: VerseEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(verse)
        }
    }

    // Audio Playback with Media3 ExoPlayer
    fun playCurrentChapterAudio() {
        val bookInfo = BibleCatalog.findBook(_selectedBook.value)
        val title = "${bookInfo.nameForLanguage(_currentLanguage.value)} Chapter ${_selectedChapter.value}"
        val subtitle = "Audio Bible Stream - ${bookInfo.testament} Testament"
        val url = BibleCatalog.getAudioStreamUrl(_selectedBook.value, _selectedChapter.value)
        audioPlayerManager.playChapterAudio(title, subtitle, url)
    }

    fun playMediaItem(item: MediaItem) {
        audioPlayerManager.playMedia(item)
    }

    fun playKidsStoryNarration(storyTitle: String, pageNumber: Int) {
        val streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"
        audioPlayerManager.playChapterAudio(
            title = storyTitle,
            subtitle = "Kids Story Audio Narration - Page $pageNumber",
            streamUrl = streamUrl
        )
    }

    fun toggleAudioPlayPause() {
        audioPlayerManager.togglePlayPause()
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

    fun startStoryQuiz(story: KidsStory) {
        _activeQuizStory.value = story
    }

    fun closeStoryQuiz() {
        _activeQuizStory.value = null
    }

    fun submitQuizResult(storyId: String, correctCount: Int, totalCount: Int) {
        val total = if (totalCount <= 0) 1 else totalCount
        val percentage = ((correctCount.toFloat() / total) * 100).toInt()
        val marks = percentage
        val (grade, stars) = when {
            percentage >= 90 -> "A+" to 3
            percentage >= 70 -> "A" to 3
            percentage >= 50 -> "B" to 2
            else -> "C" to 1
        }
        val result = StoryQuizResult(
            storyId = storyId,
            totalQuestions = total,
            correctAnswers = correctCount,
            scoreMarks = marks,
            maxMarks = 100,
            grade = grade,
            starsEarned = stars
        )
        _quizScores.value = _quizScores.value + (storyId to result)
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

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }
}
