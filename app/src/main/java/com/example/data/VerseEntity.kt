package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses")
data class VerseEntity(
    @PrimaryKey val id: String, // e.g. "John_1_1"
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

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reference: String,
    val textEnglish: String,
    val textTamil: String,
    val textTelugu: String,
    val isMastered: Boolean = false,
    val streakDays: Int = 1
)

@Entity(tableName = "settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val languageCode: String = "en",
    val dailyNotificationEnabled: Boolean = true,
    val notificationTime: String = "07:00",
    val fontSizeSp: Int = 18,
    val fontStyleSerif: Boolean = true
)
