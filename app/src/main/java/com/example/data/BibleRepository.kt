package com.example.data

import com.example.model.FlashcardItem
import com.example.model.KidsStory
import com.example.model.KidsStoryPage
import com.example.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BibleRepository(private val dao: BibleDao) {

    fun getVerses(book: String, chapter: Int): Flow<List<VerseEntity>> = dao.getVerses(book, chapter)

    fun getBookmarkedVerses(): Flow<List<VerseEntity>> = dao.getBookmarkedVerses()

    suspend fun toggleBookmark(verse: VerseEntity) {
        dao.updateVerse(verse.copy(isBookmarked = !verse.isBookmarked))
    }

    suspend fun saveNote(verse: VerseEntity, noteText: String) {
        dao.updateVerse(verse.copy(note = noteText))
    }

    fun getAllFlashcards(): Flow<List<FlashcardEntity>> = dao.getAllFlashcards()

    suspend fun insertFlashcard(flashcard: FlashcardEntity) = dao.insertFlashcard(flashcard)

    suspend fun updateFlashcard(flashcard: FlashcardEntity) = dao.updateFlashcard(flashcard)

    suspend fun deleteFlashcard(id: Int) = dao.deleteFlashcard(id)

    fun getSettings(): Flow<UserSettingsEntity?> = dao.getSettings()

    suspend fun saveSettings(settings: UserSettingsEntity) = dao.insertSettings(settings)

    suspend fun seedInitialDataIfNeeded() {
        // Pre-populate sample flashcards if empty
        val sampleFlashcards = listOf(
            FlashcardEntity(
                id = 1,
                reference = "Psalm 23:1",
                textEnglish = "The Lord is my shepherd; I shall not want.",
                textTamil = "கர்த்தர் என் மேய்ப்பராயிருக்கிறார்; நான் தாழ்ச்சியடையேன்.",
                textTelugu = "యెహోవా నా కాపరి, నాకు లేమి కలుగదు.",
                isMastered = false,
                streakDays = 5
            ),
            FlashcardEntity(
                id = 2,
                reference = "John 11:35",
                textEnglish = "Jesus wept.",
                textTamil = "இயேசு கண்ணீர் விட்டார்.",
                textTelugu = "యేసు ఏడ్చెను.",
                isMastered = true,
                streakDays = 12
            ),
            FlashcardEntity(
                id = 3,
                reference = "Philippians 4:13",
                textEnglish = "I can do all things through Christ which strengtheneth me.",
                textTamil = "என்னைப் பலப்படுத்துகிற கிறிஸ்துவினாலே எல்லாவற்றையும் செய்ய எனக்குப் பெலனுண்டு.",
                textTelugu = "నన్ను బలపరచువానియందే నేను సమస్తమును చేయజాలుదును.",
                isMastered = false,
                streakDays = 3
            )
        )

        for (card in sampleFlashcards) {
            dao.insertFlashcard(card)
        }

        // Pre-populate John 1 verses
        val john1Verses = listOf(
            VerseEntity(
                id = "John_1_1",
                book = "John",
                chapter = 1,
                verseNumber = 1,
                textEnglish = "In the beginning was the Word, and the Word was with God, and the Word was God.",
                textTamil = "ஆதியிலே வார்த்தை இருந்தது, அந்த வார்த்தை தேவனிடத்திலிருந்தது, அந்த வார்த்தை தேவனாயிருந்தது.",
                textTelugu = "ఆదియందు వాక్యము ఉండెను, వాక్యము దేవునియొద్ద ఉండెను, వాక్యము దేవుడై యుండెను."
            ),
            VerseEntity(
                id = "John_1_2",
                book = "John",
                chapter = 1,
                verseNumber = 2,
                textEnglish = "The same was in the beginning with God.",
                textTamil = "அவர் ஆதியிலே தேவனிடத்திலிருந்தார்.",
                textTelugu = "ఆయన ఆదియందు దేవునియొద్ద ఉండెను."
            ),
            VerseEntity(
                id = "John_1_3",
                book = "John",
                chapter = 1,
                verseNumber = 3,
                textEnglish = "All things were made by him; and without him was not any thing made that was made.",
                textTamil = "சகலமும் அவர் மூலமாய் உண்டாயிற்று; உண்டானதொன்றும் அவராலேயல்லாமல் உண்டாகவில்லை.",
                textTelugu = "సమస్తమును ఆయన మూలముగా కలిగెను; కలిగియున్నదేదియు ఆయనలేకుండా కలగలేదు."
            ),
            VerseEntity(
                id = "John_1_4",
                book = "John",
                chapter = 1,
                verseNumber = 4,
                textEnglish = "In him was life; and the life was the light of men.",
                textTamil = "அவருக்குள் ஜீவன் இருந்தது, அந்த ஜீவன் மனிதருக்கு வெளிச்சமாயிருந்தது.",
                textTelugu = "ఆయనలో జీవము ఉండెను; ఆ జీవము మనుష్యుల వెలుగై యుండెను."
            ),
            VerseEntity(
                id = "John_1_5",
                book = "John",
                chapter = 1,
                verseNumber = 5,
                textEnglish = "And the light shineth in darkness; and the darkness comprehended it not.",
                textTamil = "அந்த வெளிச்சம் இருளிலே பிரகாசிக்கிறது; இருளானது அதைப்பற்றிக்கொள்ளவில்லை.",
                textTelugu = "ఆ వెలుగు చీకటిలో ప్రకాశించుచున్నది గాని చీకటి దానిని గ్రహింపకుండెను."
            ),
            VerseEntity(
                id = "John_1_6",
                book = "John",
                chapter = 1,
                verseNumber = 6,
                textEnglish = "There was a man sent from God, whose name was John.",
                textTamil = "தேவனால் அனுப்பப்பட்ட ஒரு மனிதன் இருந்தான், அவன் பெயர் யோவான்.",
                textTelugu = "దేవునియొద్దనుండి పంపబడిన యొక మనుష్యుడు ఉండెను; అతని పేరు యోహాను."
            ),
            VerseEntity(
                id = "John_1_7",
                book = "John",
                chapter = 1,
                verseNumber = 7,
                textEnglish = "The same came for a witness, to bear witness of the Light, that all men through him might believe.",
                textTamil = "அவன் எல்லாரும் தன் மூலமாய் விசுவாசிக்கும்படி அந்த வெளிச்சத்தைக் குறித்துச் சாட்சி கொடுக்கச் சாட்சியாக வந்தான்.",
                textTelugu = "అతని మూలముగా అందరు విశ్వసించునట్లు అతడు ఆ వెలుగునుగూర్చి సాక్ష్యమిచ్చుటకు సాక్షిగా వచ్చెను."
            )
        )

        dao.insertVerses(john1Verses)

        // Seed default settings if empty
        dao.insertSettings(
            UserSettingsEntity(
                id = 1,
                languageCode = "en",
                dailyNotificationEnabled = true,
                notificationTime = "07:00",
                fontSizeSp = 18,
                fontStyleSerif = true
            )
        )
    }

    // Static sample Kids Stories with CDN hotlinked images matching requested UI
    fun getKidsStories(): List<KidsStory> {
        return listOf(
            KidsStory(
                id = "noahs_ark",
                title = "Noah's Ark",
                subtitle = "God keeps His promise to Noah and his family in this classic tale of faith and a giant boat.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDqSDBEnYsYB330fAiNB5c0XPuqKJEQupW9ztQ9M6nHezkTCtiNvJfUr6Xn1CqiFl7s9wYS0LMAAzSXUmEXhlM3daq_16aYFAs4DJSjfQh6_-1zV-6lzfW8xBLRtmkvZwsx2GantQRAsWOvhImrE27rmm2GldjitriXhdV4SZU1pXV1hajopFtdzR7Zisj_4zc9WbCOoGTBc4gZKv69kZ1spptPjeJ0nibRtAuWSWeg_WuP7D3RlRMIgQ",
                badgeIcon = "explore",
                badgeColorHex = 0xFFF5A623,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headline = "God's Special Call to Noah",
                        text = "Noah was a good man who walked with God. When God decided to protect Noah's family, He asked Noah to build a massive wooden ark!",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDqSDBEnYsYB330fAiNB5c0XPuqKJEQupW9ztQ9M6nHezkTCtiNvJfUr6Xn1CqiFl7s9wYS0LMAAzSXUmEXhlM3daq_16aYFAs4DJSjfQh6_-1zV-6lzfW8xBLRtmkvZwsx2GantQRAsWOvhImrE27rmm2GldjitriXhdV4SZU1pXV1hajopFtdzR7Zisj_4zc9WbCOoGTBc4gZKv69kZ1spptPjeJ0nibRtAuWSWeg_WuP7D3RlRMIgQ",
                        memoryVerse = "Genesis 6:8 - But Noah found grace in the eyes of the Lord.",
                        quizQuestion = "What did God ask Noah to build?",
                        quizOptions = listOf("A Giant Tower", "A Wooden Ark", "A Castle"),
                        correctOptionIndex = 1
                    ),
                    KidsStoryPage(
                        pageNumber = 2,
                        headline = "Animals Two by Two",
                        text = "Giraffes, elephants, lions, and birds walked up the wooden ramp into the Ark, two by two, safe and cozy!",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDqSDBEnYsYB330fAiNB5c0XPuqKJEQupW9ztQ9M6nHezkTCtiNvJfUr6Xn1CqiFl7s9wYS0LMAAzSXUmEXhlM3daq_16aYFAs4DJSjfQh6_-1zV-6lzfW8xBLRtmkvZwsx2GantQRAsWOvhImrE27rmm2GldjitriXhdV4SZU1pXV1hajopFtdzR7Zisj_4zc9WbCOoGTBc4gZKv69kZ1spptPjeJ0nibRtAuWSWeg_WuP7D3RlRMIgQ",
                        memoryVerse = "Genesis 7:9 - There went in two and two unto Noah into the ark.",
                        quizQuestion = "How did the animals enter the ark?",
                        quizOptions = listOf("Two by Two", "All at Once", "In alphabetical order"),
                        correctOptionIndex = 0
                    ),
                    KidsStoryPage(
                        pageNumber = 3,
                        headline = "The Golden Rainbow Promise",
                        text = "After the rain stopped, God placed a vibrant rainbow in the sky as a forever promise of His love and faithfulness!",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDqSDBEnYsYB330fAiNB5c0XPuqKJEQupW9ztQ9M6nHezkTCtiNvJfUr6Xn1CqiFl7s9wYS0LMAAzSXUmEXhlM3daq_16aYFAs4DJSjfQh6_-1zV-6lzfW8xBLRtmkvZwsx2GantQRAsWOvhImrE27rmm2GldjitriXhdV4SZU1pXV1hajopFtdzR7Zisj_4zc9WbCOoGTBc4gZKv69kZ1spptPjeJ0nibRtAuWSWeg_WuP7D3RlRMIgQ",
                        memoryVerse = "Genesis 9:13 - I do set my bow in the cloud, and it shall be for a token of a covenant.",
                        quizQuestion = "What sign did God place in the sky?",
                        quizOptions = listOf("A Comet", "A Rainbow", "A Shooting Star"),
                        correctOptionIndex = 1
                    )
                )
            ),
            KidsStory(
                id = "david_goliath",
                title = "David & Goliath",
                subtitle = "A young shepherd boy faces a giant with just a sling and faith.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3ODySxaaSY97jqJ6dHhZXQTGd5v5rGs1GT_0EX5VNpTNo1mlMBG63lg-s9NmIRZOZNvMTQ2metKl0uPEFMdlIL3jKD5oRHfDwjEk2RpVB7T_f0l7K1SAXzr_ke1dgPXm-lY_8VG3OI_0Bp1EGsK4p5xNo6Ks4H18cuNHWfstb5B7k_Tw0I_xEF_cGkDZ3wWrqOtPY6jfBtt71YVusLAcPUN_mcDRiYFn-V66XQanTipDetliaCRa6OA",
                badgeIcon = "bolt",
                badgeColorHex = 0xFFF5A623,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headline = "The Brave Shepherd Boy",
                        text = "Young David guarded his sheep and trusted God. When giant Goliath challenged the army, David stepped forward courageously!",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3ODySxaaSY97jqJ6dHhZXQTGd5v5rGs1GT_0EX5VNpTNo1mlMBG63lg-s9NmIRZOZNvMTQ2metKl0uPEFMdlIL3jKD5oRHfDwjEk2RpVB7T_f0l7K1SAXzr_ke1dgPXm-lY_8VG3OI_0Bp1EGsK4p5xNo6Ks4H18cuNHWfstb5B7k_Tw0I_xEF_cGkDZ3wWrqOtPY6jfBtt71YVusLAcPUN_mcDRiYFn-V66XQanTipDetliaCRa6OA",
                        memoryVerse = "1 Samuel 17:45 - The Lord saveth not with sword and spear; for the battle is the Lord's.",
                        quizQuestion = "What weapons did David bring to fight Goliath?",
                        quizOptions = listOf("A Heavy Armor", "A Sling & 5 Smooth Stones", "A Cannon"),
                        correctOptionIndex = 1
                    )
                )
            ),
            KidsStory(
                id = "daniels_faith",
                title = "Daniel's Faith",
                subtitle = "Protected by angels in a den of hungry lions.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBgNUBvJWGmT5XjxBwMvSr319lcgREk9Ns0dymEqPO9RQ8OC3P8nDTOy9fUJ9XK0mwN_vogpm3T7Fpt8pToO-Q-OV_bjHpy3XC9k52z9xPrzXmUWY82WiZkQmYg7gaddo6YzCua6yS6AJtqqpGWttALAHBpM4LiwkPlADW4qoNT3PDMlNTw-YKtn8T7exKxgnKw7qa17iUuy7I9WcZluwsZFdDJPwVytVnnkOivPsftGxZpIe6QRZ2vtg",
                badgeIcon = "shield",
                badgeColorHex = 0xFF50E3C2,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headline = "Peace in the Den",
                        text = "Daniel prayed three times a day. Even when thrown into the lions' den, God sent His angel to shut the lions' mouths!",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBgNUBvJWGmT5XjxBwMvSr319lcgREk9Ns0dymEqPO9RQ8OC3P8nDTOy9fUJ9XK0mwN_vogpm3T7Fpt8pToO-Q-OV_bjHpy3XC9k52z9xPrzXmUWY82WiZkQmYg7gaddo6YzCua6yS6AJtqqpGWttALAHBpM4LiwkPlADW4qoNT3PDMlNTw-YKtn8T7exKxgnKw7qa17iUuy7I9WcZluwsZFdDJPwVytVnnkOivPsftGxZpIe6QRZ2vtg",
                        memoryVerse = "Daniel 6:22 - My God hath sent his angel, and hath shut the lions' mouths.",
                        quizQuestion = "Who closed the mouths of the lions?",
                        quizOptions = listOf("An Angel sent by God", "The King", "The Guards"),
                        correctOptionIndex = 0
                    )
                )
            )
        )
    }

    fun getSampleMedia(): List<MediaItem> {
        return listOf(
            MediaItem(
                id = "m1",
                title = "The Light of the World",
                speakerOrArtist = "TPM Pastor Message",
                category = "Sermon",
                duration = "24:15",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1509021436468-d51030076a08?w=500"
            ),
            MediaItem(
                id = "m2",
                title = "Abide With Me (Hymn #142)",
                speakerOrArtist = "Sanctuary Choir",
                category = "Hymn",
                duration = "04:32",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500"
            ),
            MediaItem(
                id = "m3",
                title = "Psalm 23 Meditative Audio Reading",
                speakerOrArtist = "Audio Scripture",
                category = "Devotional",
                duration = "03:10",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=500"
            ),
            MediaItem(
                id = "m4",
                title = "Faith like a Mustard Seed",
                speakerOrArtist = "Sunday Worship Sermon",
                category = "Sermon",
                duration = "32:00",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
                thumbnailUrl = "https://images.unsplash.com/photo-1438232992991-995b7058bbb3?w=500"
            )
        )
    }
}
