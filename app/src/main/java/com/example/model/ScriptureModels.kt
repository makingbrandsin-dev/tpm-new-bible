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

data class KidsStory(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val badgeIcon: String, // e.g. "new_releases", "bolt", "shield"
    val badgeColorHex: Long, // Color tint hex
    val pages: List<KidsStoryPage>
)

data class KidsStoryPage(
    val pageNumber: Int,
    val headline: String,
    val text: String,
    val imageUrl: String,
    val memoryVerse: String? = null,
    val quizQuestion: String? = null,
    val quizOptions: List<String>? = null,
    val correctOptionIndex: Int? = null
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
