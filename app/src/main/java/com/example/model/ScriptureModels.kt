package com.example.model

data class ScriptureVerse(
    val id: String, // e.g. "John_1_1"
    val book: String,
    val chapter: Int,
    val verseNumber: Int,
    val textEnglish: String,
    val textTamil: String,
    val textTelugu: String,
    val isBookmarked: Boolean = false,
    val isMastered: Boolean = false,
    val note: String? = null
)

data class BibleBook(
    val id: String,
    val name: String,
    val testament: String, // "Old" or "New"
    val totalChapters: Int
)

data class QuizQuestion(
    val id: Int,
    val questionEnglish: String,
    val questionTamil: String,
    val questionTelugu: String,
    val optionsEnglish: List<String>,
    val optionsTamil: List<String>,
    val optionsTelugu: List<String>,
    val correctOptionIndex: Int,
    val teacherExplanationEnglish: String,
    val teacherExplanationTamil: String,
    val teacherExplanationTelugu: String
) {
    fun question(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> questionEnglish
        AppLanguage.TAMIL -> questionTamil
        AppLanguage.TELUGU -> questionTelugu
    }

    fun options(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> optionsEnglish
        AppLanguage.TAMIL -> optionsTamil
        AppLanguage.TELUGU -> optionsTelugu
    }

    fun teacherExplanation(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> teacherExplanationEnglish
        AppLanguage.TAMIL -> teacherExplanationTamil
        AppLanguage.TELUGU -> teacherExplanationTelugu
    }
}

data class KidsStoryPage(
    val pageNumber: Int,
    val headlineEnglish: String,
    val headlineTamil: String,
    val headlineTelugu: String,
    val textEnglish: String,
    val textTamil: String,
    val textTelugu: String,
    val teacherNoteEnglish: String,
    val teacherNoteTamil: String,
    val teacherNoteTelugu: String,
    val imageUrl: String,
    val memoryVerseEnglish: String? = null,
    val memoryVerseTamil: String? = null,
    val memoryVerseTelugu: String? = null
) {
    fun headline(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> headlineEnglish
        AppLanguage.TAMIL -> headlineTamil
        AppLanguage.TELUGU -> headlineTelugu
    }

    fun text(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> textEnglish
        AppLanguage.TAMIL -> textTamil
        AppLanguage.TELUGU -> textTelugu
    }

    fun teacherNote(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> teacherNoteEnglish
        AppLanguage.TAMIL -> teacherNoteTamil
        AppLanguage.TELUGU -> teacherNoteTelugu
    }

    fun memoryVerse(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> memoryVerseEnglish
        AppLanguage.TAMIL -> memoryVerseTamil
        AppLanguage.TELUGU -> memoryVerseTelugu
    }
}

data class KidsStory(
    val id: String,
    val titleEnglish: String,
    val titleTamil: String,
    val titleTelugu: String,
    val subtitleEnglish: String,
    val subtitleTamil: String,
    val subtitleTelugu: String,
    val imageUrl: String,
    val category: String, // "Old Testament", "New Testament", "Parables", "Miracles"
    val badgeIcon: String, // "explore", "bolt", "shield", "star", "heart"
    val badgeColorHex: Long,
    val pages: List<KidsStoryPage>,
    val quizQuestions: List<QuizQuestion> = emptyList()
) {
    fun title(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> titleEnglish
        AppLanguage.TAMIL -> titleTamil
        AppLanguage.TELUGU -> titleTelugu
    }

    fun subtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> subtitleEnglish
        AppLanguage.TAMIL -> subtitleTamil
        AppLanguage.TELUGU -> subtitleTelugu
    }
}

data class StoryQuizResult(
    val storyId: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val scoreMarks: Int, // e.g. 100 Marks
    val maxMarks: Int = 100,
    val grade: String, // "A+", "A", "B", "C"
    val starsEarned: Int // 1 to 3
)

data class MediaItem(
    val id: String,
    val title: String,
    val speakerOrArtist: String,
    val category: String, // "Sermon", "Hymn", "Devotional", "Children"
    val duration: String,
    val audioUrl: String,
    val thumbnailUrl: String
)

data class FlashcardItem(
    val id: Int = 0,
    val reference: String, // "Psalm 23:1"
    val textEnglish: String,
    val textTamil: String,
    val textTelugu: String,
    val isMastered: Boolean = false,
    val reviewStreak: Int = 0
)

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    TAMIL("ta", "Tamil"),
    TELUGU("te", "Telugu")
}
