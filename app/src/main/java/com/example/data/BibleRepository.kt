package com.example.data

import com.example.model.FlashcardItem
import com.example.model.KidsStory
import com.example.model.KidsStoryPage
import com.example.model.MediaItem
import com.example.model.QuizQuestion
import kotlinx.coroutines.flow.Flow

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
            )
        )

        dao.insertVerses(john1Verses)

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

    // Full catalog of 100 Kids Bible Stories in 3 languages (English, Tamil, Telugu) with Teacher explanations & Quizzes
    fun getKidsStories(): List<KidsStory> {
        return KidsCatalog.get100KidsStories()
    }

    private fun oldStoriesList(): List<KidsStory> {
        return emptyList()
    }
    /*
    fun oldList() = listOf(
        KidsStory(
            id = "creation",
                titleEnglish = "Creation of the World",
                titleTamil = "உலகத்தைப் படைத்தல்",
                titleTelugu = "ప్రపంచ సృష్టి",
                subtitleEnglish = "God made the light, stars, seas, animals, and humanity with loving power.",
                subtitleTamil = "தேவன் தமது அன்பினாலே வெளிச்சம், நட்சத்திரங்கள், கடல்கள், விலங்குகள் மற்றும் மனிதரைப் படைத்தார்.",
                subtitleTelugu = "దేవుడు తన ప్రేమతో వెలుగు, నక్షత్రాలు, సముద్రాలు, జంతువులు మరియు మానవులను సృష్టించాడు.",
                imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800",
                category = "Old Testament",
                badgeIcon = "explore",
                badgeColorHex = 0xFFFFB300,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "In the Beginning",
                        headlineTamil = "ஆதியிலே...",
                        headlineTelugu = "ఆదియందు...",
                        textEnglish = "In the beginning, before anything existed, God created the heavens and the earth! He spoke softly, 'Let there be light!', and brilliant light filled the darkness.",
                        textTamil = "ஆதியிலே, எதுவுமே இல்லாதபோது, தேவன் வானத்தையும் பூமியையும் படைத்தார்! 'வெளிச்சம் உண்டாகக்கடவது' என்று அவர் சொன்னார், பிரகாசமான வெளிச்சம் இருளை நிரப்பியது.",
                        textTelugu = "ఆదియందు, ఏదీ లేనప్పుడు, దేవుడు ఆకాశమును భూమిని సృష్టించెను! 'వెలుగు కలుగును గాక' అని ఆయన చెప్పగా, కాంతి చీకటిని నింపెను.",
                        teacherNoteEnglish = "Teacher Grace says: Children, God is the ultimate Creator! Everything beautiful you see around you—the bright sun, green trees, and blue sky—comes from His great love.",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: குழந்தைகளே, தேவனே மிகப்பெரிய படைப்பாளி! நீங்கள் சுற்றிலும் பார்க்கும் சூரியன், மரங்கள், நீல வானம் எல்லாமே அவருடைய அன்பின் வெளிப்பாடு.",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: పిల్లలు, దేవుడే సృష్టికర్త! మీరు చూసే ప్రతి అందమైనది దేవుని గొప్ప ప్రేమ నుండి వచ్చింది.",
                        imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800",
                        memoryVerseEnglish = "Genesis 1:1 - In the beginning God created the heaven and the earth.",
                        memoryVerseTamil = "ஆதியாகமம் 1:1 - ஆதியிலே தேவன் வானத்தையும் பூமியையும் படைத்தார்.",
                        memoryVerseTelugu = "ఆదికాండము 1:1 - ఆదియందు దేవుడు భూమ్యాకాశములను సృష్టించెను."
                    ),
                    KidsStoryPage(
                        pageNumber = 2,
                        headlineEnglish = "Animals & Beautiful Garden",
                        headlineTamil = "விலங்குகளும் அழகான தோட்டமும்",
                        headlineTelugu = "జంతువులు మరియు అందమైన తోట",
                        textEnglish = "God made swimming fish, soaring eagles, gentle deer, and roaring lions. Then God made the first human, Adam, and placed him in the Garden of Eden.",
                        textTamil = "தேவன் மீன்கள், பறவைகள், சிங்கங்கள் மற்றும் எல்லா ஜீவராசிகளையும் படைத்தார். பின்னர் முதல் மனிதனான ஆதாமைப் படைத்து ஏதேன் தோட்டத்தில் வைத்தார்.",
                        textTelugu = "దేవుడు చేపలు, పక్షులు, సింహాలు మరియు అన్ని ప్రాణులను సృష్టించాడు. అప్పుడు మొదటి మానవుడైన ఆదామును సృష్టించి ఏదెను తోటలో ఉంచాడు.",
                        teacherNoteEnglish = "Teacher Grace says: God cares for every creature He made, and He created you in His image to be special and loved!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: தேவன் படைத்த ஒவ்வொரு உயிரினத்தையும் நேசிக்கிறார்! உங்களையும் தனித்துவமாகப் படைத்துள்ளார்.",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: దేవుడు తన సృష్టిలోని ప్రతిదానిని ప్రేమిస్తాడు, నిన్ను కూడా ప్రత్యేకంగా సృష్టించాడు!",
                        imageUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=800",
                        memoryVerseEnglish = "Psalm 104:24 - O Lord, how manifold are thy works!",
                        memoryVerseTamil = "சங்கீதம் 104:24 - கர்த்தாவே, உமது கிரியைகள் எவ்வளவு திரளாயிருக்கிறது!",
                        memoryVerseTelugu = "కీర్తనలు 104:24 - యెహోవా, నీ కార్యములు ఎంత బహువిధములుగా ఉన్నవి!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "What did God create on the very first day?",
                        questionTamil = "முதல் நாளில் தேவன் எதைப் படைத்தார்?",
                        questionTelugu = "మొదటి రోజున దేవుడు ఏమి సృష్టించాడు?",
                        optionsEnglish = listOf("Moons", "Light", "Robots"),
                        optionsTamil = listOf("சந்திரன்", "வெளிச்சம்", "இயந்திரங்கள்"),
                        optionsTelugu = listOf("చంద్రుడు", "వెలుగు", "యంత్రాలు"),
                        correctOptionIndex = 1,
                        teacherExplanationEnglish = "Correct! God said 'Let there be light!' and light appeared.",
                        teacherExplanationTamil = "சரி! 'வெளிச்சம் உண்டாகக்கடவது' என்று தேவன் சொன்னார்.",
                        teacherExplanationTelugu = "సరైనది! దేవుడు 'వెలుగు కలుగును గాక' అని చెప్పారు."
                    ),
                    QuizQuestion(
                        id = 2,
                        questionEnglish = "Where did God place Adam to live happily?",
                        questionTamil = "ஆதாமை வாழ வைப்பதற்கு தேவன் எந்தத் தோட்டத்தை அமைத்தார்?",
                        questionTelugu = "ఆదాము నివసించడానికి దేవుడు ఏ తోటను ఏర్పాటు చేశాడు?",
                        optionsEnglish = listOf("Garden of Eden", "Desert", "Moon"),
                        optionsTamil = listOf("ஏதேன் தோட்டம்", "பாலைவனம்", "சந்திரன்"),
                        optionsTelugu = listOf("ఏదెను తోట", "ఎడారి", "చంద్రుడు"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Great job! Adam lived in the lush Garden of Eden.",
                        teacherExplanationTamil = "சிறப்பு! ஆதாம் ஏதேன் தோட்டத்தில் வாழ்ந்தார்.",
                        teacherExplanationTelugu = "చాలా మంచిది! ఆదాము ఏదెను తోటలో నివసించాడు."
                    )
                )
            ),

            // 2. Noah's Ark
            KidsStory(
                id = "noahs_ark",
                titleEnglish = "Noah's Ark & The Rainbow",
                titleTamil = "நோவாவின் பேழை & வானவில் வாக்குத்தத்தம்",
                titleTelugu = "నోవా ఓడ మరియు రంగుల విల్లు",
                subtitleEnglish = "God keeps His promise to Noah and his family in this classic tale of faith.",
                subtitleTamil = "நோவாவின் விசுவாசத்தின் மூலம் தேவன் தமது வாக்குத்தத்தத்தைக் காப்பாற்றுகிறார்.",
                subtitleTelugu = "నోవా విశ్వాసం ద్వారా దేవుడు తన వాగ్దానాన్ని నెరవేర్చాడు.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDqSDBEnYsYB330fAiNB5c0XPuqKJEQupW9ztQ9M6nHezkTCtiNvJfUr6Xn1CqiFl7s9wYS0LMAAzSXUmEXhlM3daq_16aYFAs4DJSjfQh6_-1zV-6lzfW8xBLRtmkvZwsx2GantQRAsWOvhImrE27rmm2GldjitriXhdV4SZU1pXV1hajopFtdzR7Zisj_4zc9WbCOoGTBc4gZKv69kZ1spptPjeJ0nibRtAuWSWeg_WuP7D3RlRMIgQ",
                category = "Old Testament",
                badgeIcon = "explore",
                badgeColorHex = 0xFFF5A623,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "God's Call to Noah",
                        headlineTamil = "நோவாவுக்கு தேவனுடைய அழைப்பு",
                        headlineTelugu = "నోవాకు దేవుని పిలుపు",
                        textEnglish = "Noah was a faithful man who obeyed God. God instructed Noah to construct a massive wooden boat called an Ark to protect his family and animals from a flood.",
                        textTamil = "நோவா தேவனுக்குக் கீழ்ப்படிந்த நல்ல மனிதர். பெருவெள்ளத்தில் இருந்து குடும்பத்தையும் விலங்குகளையும் காப்பாற்ற பெரிய மரப் பேழையைக் கட்ட தேவன் நோவாவுக்குக் கட்டளையிட்டார்.",
                        textTelugu = "నోవా దేవునికి విధేయత చూపిన మంచి మనుష్యుడు. ప్రళయం నుండి కుటుంబాన్ని, జంతువులను కాపాడటానికి ఒక పెద్ద చెక్క ఓడను నిర్మించమని దేవుడు చెప్పాడు.",
                        teacherNoteEnglish = "Teacher Grace says: Obeying God brings safety and blessing! Just like Noah trusted God when building the ark, we can trust God in everything we do.",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: தேவனுக்குக் கீழ்ப்படிவது பாதுகாப்பையும் ஆசீர்வாதத்தையும் தரும்! நோவா தேவனை நம்பியது போல நாமும் தேவனை எப்போதும் நம்ப வேண்டும்.",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: దేవునికి విధేయత చూపడం రక్షణను ఇస్తుంది! నోవాలాగా మనం కూడా దேవునిపై నమ్మకం ఉంచాలి.",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDqSDBEnYsYB330fAiNB5c0XPuqKJEQupW9ztQ9M6nHezkTCtiNvJfUr6Xn1CqiFl7s9wYS0LMAAzSXUmEXhlM3daq_16aYFAs4DJSjfQh6_-1zV-6lzfW8xBLRtmkvZwsx2GantQRAsWOvhImrE27rmm2GldjitriXhdV4SZU1pXV1hajopFtdzR7Zisj_4zc9WbCOoGTBc4gZKv69kZ1spptPjeJ0nibRtAuWSWeg_WuP7D3RlRMIgQ",
                        memoryVerseEnglish = "Genesis 6:8 - But Noah found grace in the eyes of the Lord.",
                        memoryVerseTamil = "ஆதியாகமம் 6:8 - நோவாவுக்கோ கர்த்தருடைய கண்களில் கிருபை கிடைத்தது.",
                        memoryVerseTelugu = "ఆదికాండము 6:8 - అయితే నోవా యెహోవా దృష్టియందు కృప పొందినవాడాయెను."
                    ),
                    KidsStoryPage(
                        pageNumber = 2,
                        headlineEnglish = "Animals Two by Two",
                        headlineTamil = "ஜோடி ஜோடியாக விலங்குகள்",
                        headlineTelugu = "రెండేసి చొప్పున జంతువులు",
                        textEnglish = "Elephants, lions, giraffes, and birds walked up into the ark two by two. God closed the door safely, and the rain fell, but everyone inside was warm and safe!",
                        textTamil = "யானைகள், சிங்கங்கள், ஒட்டகச்சிவிங்கிகள் மற்றும் பறவைகள் ஜோடி ஜோடியாக பேழைக்குள் சென்றன. தேவன் கதவை மூடினார், மழை பெய்தபோதும் அனைவரும் பாதுகாப்பாக இருந்தனர்!",
                        textTelugu = "ఏనుగులు, సింహాలు, జిరాఫీలు మరియు పక్షులు జంటలుగా ఓడలోకి వెళ్లాయి. దేవుడు తలుపు మూసాడు, వర్షం పడినా లోపల ఉన్నవారు సురక్షితంగా ఉన్నారు!",
                        teacherNoteEnglish = "Teacher Grace says: When God shuts the door of protection around us, no storm can harm us!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: தேவன் நம்மைப் பாதுகாக்கும் போது எந்தப் புயலும் நம்மை அசைக்க முடியாது!",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: దేవుడు మనకు రక్షణ కల్పించినప్పుడు ఏ తుఫాను మనల్ని ఏమీ చేయలేదు!",
                        imageUrl = "https://images.unsplash.com/photo-1534567153574-2b12153a87f0?w=800",
                        memoryVerseEnglish = "Genesis 7:9 - There went in two and two unto Noah into the ark.",
                        memoryVerseTamil = "ஆதியாகமம் 7:9 - இரண்டிரண்டாக நோவாவிடத்தில் பேழைக்குள்ளே பிரவேசித்தன.",
                        memoryVerseTelugu = "ఆదికాండము 7:9 - జంటలు జంటలుగా నోవా వద్దకు ఓడలోకి వెళ్లెను."
                    ),
                    KidsStoryPage(
                        pageNumber = 3,
                        headlineEnglish = "The Beautiful Rainbow Promise",
                        headlineTamil = "அழகான வானவில் வாக்குத்தத்தம்",
                        headlineTelugu = "రంగుల విల్లు వాగ్దానం",
                        textEnglish = "When the land dried, Noah gave thanks to God. God placed a colorful rainbow in the sky as a forever promise that He would never flood the earth again.",
                        textTamil = "பூமி காய்ந்ததும் நோவா தேவனுக்கு நன்றி செலுத்தினார். இனி பூமியை வெள்ளத்தால் அழிக்கமாட்டேன் என்பதற்கு அடையாளமாக தேவன் அழகான வானவில்லை வானத்தில் வைத்தார்.",
                        textTelugu = "భూమి ఆరిపోయిన తర్వాత నోవా దేవునికి కృతజ్ఞతలు చెప్పాడు. ఇకపై భూమిని ప్రళయంతో నాశనం చేయనని దేవుడు ఆకాశంలో రంగుల విల్లును ఉంచాడు.",
                        teacherNoteEnglish = "Teacher Grace says: Every time you see a rainbow, remember that God always keeps His promises!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: நீங்கள் வானவில்லைப் பார்க்கும் போதெல்லாம், தேவன் வாக்குத்தத்தங்களை நிறைவேற்றுகிறவர் என்பதை நினைவில் வையுங்கள்!",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: మీరు ఆకాశంలో రంగుల విల్లును చూసినప్పుడల్లా దేవుడు తన వాగ్దానాలను నెరవేరుస్తాడని గుర్తుంచుకోండి!",
                        imageUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=800",
                        memoryVerseEnglish = "Genesis 9:13 - I do set my bow in the cloud, and it shall be for a token of a covenant.",
                        memoryVerseTamil = "ஆதியாகமம் 9:13 - என் வில்லை மேகத்தில் வைக்கிறேன், அது உடன்படிக்கைக்கு அடையாளமாக இருக்கும்.",
                        memoryVerseTelugu = "ఆదికాండము 9:13 - నేను నా ధనస్సును మేఘములో ఉంచుచున్నాను, అది నిబంధనకు గుర్తుగా ఉండును."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "What did God instruct Noah to build?",
                        questionTamil = "நோவா எதைக் கட்ட வேண்டுமென்று தேவன் கட்டளையிட்டார்?",
                        questionTelugu = "నోవాను ఏమి నిర్మించమని దேవుడు చెప్పాడు?",
                        optionsEnglish = listOf("Castle", "Large Wooden Ark", "Pyramid"),
                        optionsTamil = listOf("அரண்மனை", "பெரிய மரப் பேழை", "பிரமிடு"),
                        optionsTelugu = listOf("కోట", "పెద్ద చెక్క ఓడ", "పిరమిడ్"),
                        correctOptionIndex = 1,
                        teacherExplanationEnglish = "Correct! Noah built a huge wooden Ark to stay safe.",
                        teacherExplanationTamil = "சரி! நோவா பாதுகாப்பிற்காகப் பெரிய பேழையைக் கட்டினார்.",
                        teacherExplanationTelugu = "సరైనది! నోవా రక్షణ కోసం పెద్ద చెక్క ఓడను నిర్మించాడు."
                    ),
                    QuizQuestion(
                        id = 2,
                        questionEnglish = "What sign of promise did God place in the sky?",
                        questionTamil = "வானத்தில் தேவன் வைத்த வாக்குத்தத்தத்தின் அடையாளம் என்ன?",
                        questionTelugu = "ఆకాశంలో దేవుడు ఉంచిన వాగ్దాన గుర్తు ఏమిటి?",
                        optionsEnglish = listOf("Comet", "Rainbow", "Cloud Castle"),
                        optionsTamil = listOf("வால்நட்சத்திரம்", "வானவில்", "மேகக் கோட்டை"),
                        optionsTelugu = listOf("తోకచుక్క", "రంగుల విల్లు", "మేఘాల కోట"),
                        correctOptionIndex = 1,
                        teacherExplanationEnglish = "Awesome! The rainbow is God's covenant promise.",
                        teacherExplanationTamil = "அற்புதம்! வானவில் தேவனுடைய உடன்படிக்கையின் அடையாளம்.",
                        teacherExplanationTelugu = "చాలా బాగుంది! రంగుల విల్లు దేవుని నిబంధన గుర్తు."
                    )
                )
            ),

            // 3. David & Goliath
            KidsStory(
                id = "david_goliath",
                titleEnglish = "David & Goliath",
                titleTamil = "தாவீதும் கோலியாத்தும்",
                titleTelugu = "దావీదు మరియు గొల్యాతు",
                subtitleEnglish = "A young shepherd boy faces a giant with just a sling, 5 smooth stones, and unwavering faith.",
                subtitleTamil = "ஒரு சிறுவன் ஐந்து கற்கள் மற்றும் விசுவாசத்தோடு ராட்சதனை எதிர்கொண்டான்.",
                subtitleTelugu = "ఒక చిన్న గొర్రెల కాపరి విశ్వాసంతో రాక్షసుడిని ఎదుర్కొన్నాడు.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3ODySxaaSY97jqJ6dHhZXQTGd5v5rGs1GT_0EX5VNpTNo1mlMBG63lg-s9NmIRZOZNvMTQ2metKl0uPEFMdlIL3jKD5oRHfDwjEk2RpVB7T_f0l7K1SAXzr_ke1dgPXm-lY_8VG3OI_0Bp1EGsK4p5xNo6Ks4H18cuNHWfstb5B7k_Tw0I_xEF_cGkDZ3wWrqOtPY6jfBtt71YVusLAcPUN_mcDRiYFn-V66XQanTipDetliaCRa6OA",
                category = "Old Testament",
                badgeIcon = "bolt",
                badgeColorHex = 0xFFFF5252,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "The Courageous Shepherd",
                        headlineTamil = "தைரியமுள்ள மேய்ப்பன் தாவீது",
                        headlineTelugu = "ధైర్యవంతుడైన కాపరి దావీదు",
                        textEnglish = "David was a young boy who looked after sheep in the fields. While others were scared of Goliath the giant, David knew God was far bigger and stronger!",
                        textTamil = "தாவீது ஆடுகளை மேய்த்த ஒரு சிறுவன். ராட்சதனான கோலியாத்தைக் கண்டு அனைவரும் பயந்தபோது, தாவீது தேவனை நம்பி தைரியமாக இருந்தான்!",
                        textTelugu = "దావీదు పొలాల్లో గొర్రెలను కాచే చిన్న బాలుడు. రాక్షసుడైన గొల్యాతును చూసి అందరూ భయపడితే, దావీదు దేవునిపై నమ్మకంతో ధైర్యంగా ఉన్నాడు!",
                        teacherNoteEnglish = "Teacher Grace says: No matter how big your problems look, God is always bigger! You can be brave with God on your side.",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: உங்கள் சவால்கள் எவ்வளவு பெரியதாக இருந்தாலும், தேவன் அதைவிட பெரியவர்!",
                        teacherNoteTelugu = "టీچر గ్రేస్ చెప్తున్నారు: నీ సమస్యలు ఎంత పెద్దవైనా, దేవుడు అంతకంటే గొప్పవాడు!",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3ODySxaaSY97jqJ6dHhZXQTGd5v5rGs1GT_0EX5VNpTNo1mlMBG63lg-s9NmIRZOZNvMTQ2metKl0uPEFMdlIL3jKD5oRHfDwjEk2RpVB7T_f0l7K1SAXzr_ke1dgPXm-lY_8VG3OI_0Bp1EGsK4p5xNo6Ks4H18cuNHWfstb5B7k_Tw0I_xEF_cGkDZ3wWrqOtPY6jfBtt71YVusLAcPUN_mcDRiYFn-V66XQanTipDetliaCRa6OA",
                        memoryVerseEnglish = "1 Samuel 17:45 - The battle is the Lord's.",
                        memoryVerseTamil = "1 சாமுவேல் 17:45 - யுத்தம் கர்த்தருடையது.",
                        memoryVerseTelugu = "1 సమూయేలు 17:45 - యుద్ధము యెహోవాదే."
                    ),
                    KidsStoryPage(
                        pageNumber = 2,
                        headlineEnglish = "Victory in God's Name",
                        headlineTamil = "தேவனின் நாமத்தில் வெற்றி",
                        headlineTelugu = "దేవుని నామంలో విజయం",
                        textEnglish = "David selected 5 smooth stones from the stream. He loaded his sling, swung it, and the stone struck Goliath! David won because God gave him strength.",
                        textTamil = "தாவீது ஓடையில் இருந்து 5 கூழாங்கற்களை எடுத்தான். கவணில் கல்லை வைத்துச் சுழற்றி அடித்தபோது கோலியாத் விழுந்தான்! தேவன் தாவீதுக்கு வெற்றியைக் கொடுத்தார்.",
                        textTelugu = "దావీదు వాగు నుండి 5 నునుపైన రాళ్లను తీసుకున్నాడు. ఒడిసెలలో రాయి పెట్టి విసిరినప్పుడు గొల్యాతు పడిపోయాడు! దేవుడు దావీదుకు విజయాన్ని ఇచ్చాడు.",
                        teacherNoteEnglish = "Teacher Grace says: You don't need heavy swords or armor—just faith in God and the willingness to do your best!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: பெரிய வாள் தேவையில்லை, தேவனை நம்பும் விசுவாசமே போதுமானது!",
                        teacherNoteTelugu = "టీچر గ్రేస్ చెప్తున్నారు: నీకు పెద్ద ఖడ్గాలు అక్కర్లేదు, దేవునిపై విశ్వాసం ఉంటే చాలు!",
                        imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800",
                        memoryVerseEnglish = "Philippians 4:13 - I can do all things through Christ.",
                        memoryVerseTamil = "பிலிப்பியர் 4:13 - கிறிஸ்துவினாலே எல்லாவற்றையும் செய்ய எனக்குப் பெலனுண்டு.",
                        memoryVerseTelugu = "ఫిలిప్పీయులకు 4:13 - క్రీస్తునందే నేను సమస్తము చేయజాలుదును."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "What weapon did David bring to face Goliath?",
                        questionTamil = "கோலியாத்தை எதிர்கொள்ள தாவீது என்ன ஆயுதத்தைக் கொண்டுவந்தான்?",
                        questionTelugu = "గొల్యాతును ఎదుర్కోవడానికి దావీదు ఏ ఆయుధాన్ని తీసుకువచ్చాడు?",
                        optionsEnglish = listOf("Iron Sword", "Sling & 5 Smooth Stones", "Bow and Arrow"),
                        optionsTamil = listOf("இரும்பு வாள்", "கவண் & 5 கற்கள்", "வில் மற்றும் அம்பு"),
                        optionsTelugu = listOf("ఇనుప ఖడ్గం", "ఒడిసెల & 5 రాళ్లు", "విల్లు మరియు బాణం"),
                        correctOptionIndex = 1,
                        teacherExplanationEnglish = "Correct! David trusted in God with a simple sling and 5 stones.",
                        teacherExplanationTamil = "சரி! தாவீது கவணையும் 5 கற்களையும் தேவ நம்பிக்கையோடு பயன்படுத்தினான்.",
                        teacherExplanationTelugu = "సరైనది! దావీదు దేవుని నమ్మి ఒడిసెల మరియు 5 రాళ్లతో వెళ్లాడు."
                    )
                )
            ),

            // 4. Daniel in Lions' Den
            KidsStory(
                id = "daniels_faith",
                titleEnglish = "Daniel in the Lions' Den",
                titleTamil = "சிங்கங்களின் கெபியில் தானியேல்",
                titleTelugu = "సింహాల బోనులో దానియేలు",
                subtitleEnglish = "Faithful Daniel prays to God and is protected by angels in a den of lions.",
                subtitleTamil = "தானியேல் ஜெபித்தபோது தேவ தூதன் சிங்கங்களின் வாய்களைக் கட்டினான்.",
                subtitleTelugu = "దానియేలు ప్రార్థించినప్పుడు దేవుని దూత సింహాల నోళ్లను మూసివేసాడు.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBgNUBvJWGmT5XjxBwMvSr319lcgREk9Ns0dymEqPO9RQ8OC3P8nDTOy9fUJ9XK0mwN_vogpm3T7Fpt8pToO-Q-OV_bjHpy3XC9k52z9xPrzXmUWY82WiZkQmYg7gaddo6YzCua6yS6AJtqqpGWttALAHBpM4LiwkPlADW4qoNT3PDMlNTw-YKtn8T7exKxgnKw7qa17iUuy7I9WcZluwsZFdDJPwVytVnnkOivPsftGxZpIe6QRZ2vtg",
                category = "Old Testament",
                badgeIcon = "shield",
                badgeColorHex = 0xFF00E676,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "Peace in the Lions' Den",
                        headlineTamil = "சிங்கங்களின் கெபியில் அமைதி",
                        headlineTelugu = "సింహాల బోనులో ప్రశాంతత",
                        textEnglish = "Daniel loved God and prayed three times a day. Even when thrown into a pit of hungry lions, God sent His angel to close the lions' mouths completely!",
                        textTamil = "தானியேல் நாள்தோறும் மூன்று வேளை ஜெபித்தார். பசியுள்ள சிங்கங்களின் கெபியில் வீசப்பட்டபோதும், தேவன் தம் தூதனை அனுப்பி சிங்கங்களின் வாய்களை அடைத்தார்!",
                        textTelugu = "దానియేలు రోజుకు మూడుసార్లు ప్రార్థించేవాడు. ఆకలితో ఉన్న సింహాల బోనులో పడేసినా, దేవుడు తన దూతను పంపి సింహాల నోళ్లను మూయించాడు!",
                        teacherNoteEnglish = "Teacher Grace says: Never stop praying! God listens to every prayer and protects those who love Him.",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: ஜெபிப்பதை ஒருபோதும் நிறுத்தாதீர்கள்! தேவன் நம் ஜெபங்களைக் கேட்கிறார்.",
                        teacherNoteTelugu = "టీچر గ్రేస్ చెప్తున్నారు: ప్రార్థన చేయడం ఎప్పుడూ ఆపకండి! దేవుడు మన ప్రార్థనలను వింటాడు.",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBgNUBvJWGmT5XjxBwMvSr319lcgREk9Ns0dymEqPO9RQ8OC3P8nDTOy9fUJ9XK0mwN_vogpm3T7Fpt8pToO-Q-OV_bjHpy3XC9k52z9xPrzXmUWY82WiZkQmYg7gaddo6YzCua6yS6AJtqqpGWttALAHBpM4LiwkPlADW4qoNT3PDMlNTw-YKtn8T7exKxgnKw7qa17iUuy7I9WcZluwsZFdDJPwVytVnnkOivPsftGxZpIe6QRZ2vtg",
                        memoryVerseEnglish = "Daniel 6:22 - My God hath sent his angel, and hath shut the lions' mouths.",
                        memoryVerseTamil = "தானியேல் 6:22 - என் தேவன் தம்முடைய தூதனை அனுப்பி, சிங்கங்களின் வாயைக் கட்டிப்போட்டார்.",
                        memoryVerseTelugu = "దానియేలు 6:22 - నా దేవుడు తన దూతను పంపి సింహముల నోళ్లను మూయించెను."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "Who shut the mouths of the hungry lions?",
                        questionTamil = "பசியுள்ள சிங்கங்களின் வாய்களை அடைத்தது யார்?",
                        questionTelugu = "సింహాల నోళ్లను ఎవరు మూయించారు?",
                        optionsEnglish = listOf("An Angel sent by God", "The Guards", "The King"),
                        optionsTamil = listOf("தேவன் அனுப்பிய தூதன்", "காவலர்கள்", "ராஜா"),
                        optionsTelugu = listOf("దేవుడు పంపిన దూత", "రక్షకులు", "రాజు"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Praise God! An angel of God safely shut the lions' mouths.",
                        teacherExplanationTamil = "தேவனுக்கு ஸ்தோத்திரம்! தேவ தூதன் சிங்கங்களின் வாய்களை அடைத்தார்.",
                        teacherExplanationTelugu = "దేవునికి స్తోత్రం! దేవుని దూత సింహాల నోళ్లను మూయించాడు."
                    )
                )
            ),

            // 5. Moses & Red Sea
            KidsStory(
                id = "moses_red_sea",
                titleEnglish = "Moses & the Red Sea",
                titleTamil = "மோசேயும் செங்கடலும்",
                titleTelugu = "మోషే మరియు ఎర్రసముద్రం",
                subtitleEnglish = "God parts the roaring sea to lead His people safely to freedom.",
                subtitleTamil = "தேவன் செங்கடலை இரண்டாகப் பிளந்து தம் மக்களை விடுவித்தார்.",
                subtitleTelugu = "దేవుడు ఎర్రసముద్రాన్ని విడదీసి తన ప్రజలను కాపాడాడు.",
                imageUrl = "https://images.unsplash.com/photo-1509021436468-d51030076a08?w=800",
                category = "Old Testament",
                badgeIcon = "star",
                badgeColorHex = 0xFF29B6F6,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "Stand Still & See Salvation",
                        headlineTamil = "பயப்படாமல் இருங்கள்!",
                        headlineTelugu = "భయపడకుడి!",
                        textEnglish = "Trapped between Pharaoh's chariots and the deep Red Sea, Moses raised his staff. God sent a strong wind, parting the waters into two massive walls!",
                        textTamil = "பார்வோனின் படைகளுக்கும் செங்கடலுக்கும் நடுவில் நின்ற மோசே தன் கோலை உயர்த்தினார். தேவன் கடலை இரண்டாகப் பிளந்து வழி உண்டாக்கினார்!",
                        textTelugu = "ఫరో సైన్యానికి, ఎర్ரసముద్రానికి మధ్య ఉన్న మోషే తన కర్రను ఎత్తాడు. దేవుడు సముద్రాన్ని విడదీసి దారి చూపించాడు!",
                        teacherNoteEnglish = "Teacher Grace says: When you feel stuck with nowhere to turn, God will make a miracle way for you!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: வழி இல்லாத இடத்தில் தேவன் உங்களுக்கு ஒரு புதிய வழியை உண்டாக்குவார்!",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: దారి లేని చోట దేవుడు నీకు ఒక మార్గాన్ని సుగమం చేస్తాడు!",
                        imageUrl = "https://images.unsplash.com/photo-1509021436468-d51030076a08?w=800",
                        memoryVerseEnglish = "Exodus 14:14 - The Lord shall fight for you, and ye shall hold your peace.",
                        memoryVerseTamil = "யாத்திராகமம் 14:14 - கர்த்தர் உங்களுக்காக யுத்தம் பண்ணுவார்; நீங்கள் சும்மாயிருப்பீர்கள்.",
                        memoryVerseTelugu = "నిర్గమకాండము 14:14 - యెహోవా మీ తరఫున యుద్ధము చేయును, మీరు ఊరకయే ఉండవలెను."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "How did God make a path through the Red Sea?",
                        questionTamil = "செங்கடலில் தேவன் எவ்வாறு வழியை உண்டாக்கினார்?",
                        questionTelugu = "ఎర్రసముద్రంలో దేవుడు దారి ఎలా చేశాడు?",
                        optionsEnglish = listOf("Parted the waters into two walls", "Built a suspension bridge", "Flew in a cloud"),
                        optionsTamil = listOf(" தண்ணீரை இரண்டாகப் பிளந்தார்", "பாலம் கட்டினார்", "பறந்து சென்றனர்"),
                        optionsTelugu = listOf("నీటిని రెండుగా విభజించాడు", "వంతెన కట్టాడు", "విమానంలో తీసుకెళ్లాడు"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Amen! God divided the sea so His people walked on dry ground.",
                        teacherExplanationTamil = "ஆமென்! மக்கள் உலர்ந்த நிலத்தில் நடக்கக் கடலைப் பிளந்தார்.",
                        teacherExplanationTelugu = "ఆమేన్! దేవుడు సముద్రాన్ని విడదీసి ఆరిన నేలపై నడిపించాడు."
                    )
                )
            ),

            // 6. Birth of Jesus
            KidsStory(
                id = "birth_of_jesus",
                titleEnglish = "The Birth of Jesus",
                titleTamil = "இயேசுவின் பிறப்பு",
                titleTelugu = "యేసు క్రీస్తు జననం",
                subtitleEnglish = "Angels sing in joy as Savior Jesus is born in Bethlehem's cozy manger.",
                subtitleTamil = "பெத்லகேமில் இரட்சகராகிய இயேசு பிறந்தபோது தூதர்கள் துதித்துப் பாடினர்.",
                subtitleTelugu = "బెత్లహేములో రక్షకుడైన యేసు జన్మించినప్పుడు దేవదూతలు ఆనందగానం చేశారు.",
                imageUrl = "https://images.unsplash.com/photo-1512389142860-9c449e58a543?w=800",
                category = "New Testament",
                badgeIcon = "heart",
                badgeColorHex = 0xFFEC407A,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "Good News of Great Joy!",
                        headlineTamil = "நற்செய்தி மற்றும் மிகுந்த சந்தோஷம்!",
                        headlineTelugu = "సంతోషకరమైన సువార్త!",
                        textEnglish = "In quiet Bethlehem, baby Jesus was born and laid gently in a manger. Bright stars shone, and angels declared: 'For unto you is born a Saviour!'",
                        textTamil = "பெத்லகேமில் பாலன் இயேசு பிறந்தார். பிரகாசமான நட்சத்திரம் ஜொலித்தது, 'உங்களுக்கு இரட்சகர் பிறந்திருக்கிறார்' என்று வானதூதர்கள் அறிவித்தனர்.",
                        textTelugu = "బెత్లహేములో యేసు జన్మించారు. ప్రకాశవంతమైన నక్షత్రం మెరిసింది, 'మీ కొరకు రక్షకుడు జన్మించాడు' అని దేవదూతలు తెలిపారు.",
                        teacherNoteEnglish = "Teacher Grace says: Jesus is God's greatest gift of love to the whole world!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: இயேசுவே உலகிற்கு தேவன் தந்த மிகப்பெரிய அன்பின் பரிசு!",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: ప్రపంచానికి దేవుడు ఇచ్చిన అతిపెద్ద ప్రేమ కానుక యేసయ్య!",
                        imageUrl = "https://images.unsplash.com/photo-1512389142860-9c449e58a543?w=800",
                        memoryVerseEnglish = "Luke 2:11 - For unto you is born this day in the city of David a Saviour.",
                        memoryVerseTamil = "லூக்கா 2:11 - இன்று கர்த்தராகிய கிறிஸ்து என்னும் இரட்சகர் உங்களுக்குப் பிறந்திருக்கிறார்.",
                        memoryVerseTelugu = "లూకా 2:11 - దావీదు పట్టణమందు నేడు మీ కొరకు రక్షకుడు జన్మించియున్నాడు."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "In which town was baby Jesus born?",
                        questionTamil = "பாலன் இயேசு எந்த ஊரில் பிறந்தார்?",
                        questionTelugu = "యేసు బాబు ఏ ఊరిలో జన్మించాడు?",
                        optionsEnglish = listOf("Bethlehem", "Rome", "Jericho"),
                        optionsTamil = listOf("பெத்லகேம்", "ரோம்", "எரிகோ"),
                        optionsTelugu = listOf("బెత్లహేము", "రోమ్", "జెరికో"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Hallelujah! Jesus was born in Bethlehem as foretold by prophets.",
                        teacherExplanationTamil = "அல்லேலூயா! இயேசு பெத்லகேமில் பிறந்தார்.",
                        teacherExplanationTelugu = "హల్లెలూయా! ప్రవచించినట్లుగా యేసు బెత్లహేములో జన్మించారు."
                    )
                )
            ),

            // 7. Jesus Feeds 5000
            KidsStory(
                id = "jesus_feeds_5000",
                titleEnglish = "Jesus Feeds 5,000",
                titleTamil = "ஐந்தாயிரம் பேருக்கு உணவு",
                titleTelugu = "ఐదువేల మందికి భోజనం",
                subtitleEnglish = "A small boy shares 5 barley loaves and 2 small fish, and Jesus multiplies it for everyone!",
                subtitleTamil = "ஒரு சிறுவனின் 5 அப்பங்கள் மற்றும் 2 மீன்களை இயேசு பெருகப்பண்ணி அனைவருக்கும் அளித்தார்.",
                subtitleTelugu = "ఒక చిన్న బాలుడి 5 రొట్టెలు, 2 చేపలను యేసు వేలాది మందికి సరిపోయేలా చేశాడు.",
                imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=800",
                category = "Miracles",
                badgeIcon = "bolt",
                badgeColorHex = 0xFFFF9800,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "The Boy's Small Lunch",
                        headlineTamil = "சிறுவனின் சிறிய உணவு",
                        headlineTelugu = "బాలుడి చిన్న భోజనం",
                        textEnglish = "A huge crowd was listening to Jesus and got hungry. A little boy generously offered his 5 small loaves and 2 fish. Jesus blessed it, and fed over 5,000 people!",
                        textTamil = "திரளான ஜனங்கள் இயேசுவின் போதனையைக் கேட்டனர். ஒரு சிறுவன் தன் 5 அப்பங்களையும் 2 மீன்களையும் கொடுத்தான். இயேசு அதை ஆசீர்வதித்து எல்லோருக்கும் பரிமாறினார்!",
                        textTelugu = "వేలాది మంది ప్రజలు యేసు బోధలను వింటున్నారు. ఒక చిన్న బాలుడు తన 5 రొట్టెలు, 2 చేపలను ఇచ్చాడు. యేసు వాటిని ఆశీర్వదించి అందరికీ పంచాడు!",
                        teacherNoteEnglish = "Teacher Grace says: No gift is too small when placed in Jesus' hands! He can multiply what you share.",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: இயேசுவின் கையில் கொடுக்கும் எதுவும் சிறியதல்ல! அவர் அதை பெருகப்பண்ணுவார்.",
                        teacherNoteTelugu = "టీచర్ గ్రేஸ் చెప్తున్నారు: యేసయ్య చేతుల్లో పెట్టేదేదీ చిన్నది కాదు! ఆయన దాన్ని విస్తరింపజేస్తాడు.",
                        imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=800",
                        memoryVerseEnglish = "John 6:11 - And Jesus took the loaves; and when he had given thanks, he distributed to the disciples.",
                        memoryVerseTamil = "யோவான் 6:11 - இயேசு அந்த அப்பங்களை எடுத்து, ஸ்தோத்திரம்பண்ணி, சீஷர்களுக்குக் கொடுத்தார்.",
                        memoryVerseTelugu = "యోహాను 6:11 - యేసు ఆ రొట్టెలను పట్టుకొని కృతజ్ఞతాస్తుతులు చెల్లించి పంచిపెట్టెను."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "What did the little boy share with Jesus?",
                        questionTamil = "சிறுவன் இயேசுவிடம் என்ன கொடுத்தான்?",
                        questionTelugu = "చిన్న బాలుడు యేసుకు ఏమి ఇచ్చాడు?",
                        optionsEnglish = listOf("5 Loaves and 2 Fish", "10 Apples", "A Pizza"),
                        optionsTamil = listOf("5 அப்பங்கள் மற்றும் 2 மீன்கள்", "10 ஆப்பிள்கள்", "பிட்சா"),
                        optionsTelugu = listOf("5 రొట్టెలు మరియు 2 చేపలు", "10 యాపిల్స్", "పిజ్జా"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Wonderful! Jesus multiplied 5 loaves and 2 fish abundantly.",
                        teacherExplanationTamil = "சிறப்பு! இயேசு 5 அப்பங்களையும் 2 மீன்களையும் பெருகப்பண்ணினார்.",
                        teacherExplanationTelugu = "చాలా మంచిది! యేసు 5 రొట్టెలను, 2 చేపలను విస్తరింపజేశాడు."
                    )
                )
            ),

            // 8. Jesus Calms Storm
            KidsStory(
                id = "calm_storm",
                titleEnglish = "Jesus Calms the Storm",
                titleTamil = "இயேசு புயலை அடக்குதல்",
                titleTelugu = "యేసు తుఫానును శాంతింపజేయుట",
                subtitleEnglish = "Jesus speaks 'Peace, be still!' and wild waves immediately obey His command.",
                subtitleTamil = "இயேசு 'அமைதியாயிரு, அமைதலாயிரு' என்று சொன்னபோது புயல் அடங்கியது.",
                subtitleTelugu = "యేసు 'నిశ్శబ్దమై ఊరకుండుము' అని చెప్పగానే తుఫాను శాంతించింది.",
                imageUrl = "https://images.unsplash.com/photo-1505118380757-91f5f5632de0?w=800",
                category = "Miracles",
                badgeIcon = "star",
                badgeColorHex = 0xFF7C4DFF,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "Peace, Be Still!",
                        headlineTamil = "அமைதியாயிரு!",
                        headlineTelugu = "శాంతించుము!",
                        textEnglish = "While crossing the sea in a boat, a fierce storm arose. Disciples panicked, but Jesus stood up and commanded the wind and waves: 'Peace, be still!' And all became calm.",
                        textTamil = "படவில் செல்லும்போது பெரிய புயல் அடித்தது. சீஷர்கள் பயந்தனர், ஆனால் இயேசு எழுந்து 'அமைதியாயிரு, அமைதலாயிரு' என்றார். உடனே அமைதியாயிற்று.",
                        textTelugu = "పడవలో వెళ్తుండగా పెద్ద తుఫాను వచ్చింది. శిష్యులు భయపడ్డారు, కానీ యేసు లేచి 'శాంతించుము' అని ఆజ్ఞాపించగానే సముద్రం ప్రశాంతమైంది.",
                        teacherNoteEnglish = "Teacher Grace says: Whenever life feels stormy or frightening, call on Jesus. He brings real peace!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: பயம் வரும்போதெல்லாம் இயேசுவை கூப்பிடுங்கள். அவர் உங்களுக்கு சமாதானத்தைத் தருவார்!",
                        teacherNoteTelugu = "టీచర్ గ్రేஸ் చెప్తున్నారు: భయం వేసినప్పుడల్లా యేసయ్యను పిలవండి. ఆయన నీకు శాంతిని ఇస్తాడు!",
                        imageUrl = "https://images.unsplash.com/photo-1505118380757-91f5f5632de0?w=800",
                        memoryVerseEnglish = "Mark 4:39 - Peace, be still.",
                        memoryVerseTamil = "மாற்கு 4:39 - இரையாதே, அமைதலாயிரு.",
                        memoryVerseTelugu = "మార్కు 4:39 - నిశ్శబ్దమై ఊరకుండుము."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "What words did Jesus say to quiet the storm?",
                        questionTamil = "புயலை அடக்க இயேசு என்ன வார்த்தைகளைச் சொன்னார்?",
                        questionTelugu = "తుఫానును శాంతింపజేయడానికి యేసు ఏమి చెప్పాడు?",
                        optionsEnglish = listOf("Peace, be still!", "Blow faster!", "Run away!"),
                        optionsTamil = listOf("இரையாேத, அமைதலாயிரு!", "வேகமாக அடி!", "ஓடிப் போங்கள்!"),
                        optionsTelugu = listOf("శాంతించుము, నిశ్శబ్దమగుము!", "ఇంకా వేగంగా వీచు!", "పారిపోండి!"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Amen! Even the wind and seas obey His voice.",
                        teacherExplanationTamil = "ஆமென்! காற்றும் கடலும் அவருக்குக் கீழ்ப்படிகின்றன.",
                        teacherExplanationTelugu = "ఆమేన్! గాలి, సముద్రం కూడా ఆయన మాటకు లోబడతాయి."
                    )
                )
            ),

            // 9. Resurrection of Jesus
            KidsStory(
                id = "resurrection",
                titleEnglish = "The Resurrection of Jesus",
                titleTamil = "இயேசுவின் உயிர்த்தெழுதல்",
                titleTelugu = "యేసు క్రీస్తు పునరుత్థానము",
                subtitleEnglish = "He is Risen! Jesus conquers death and gives eternal life to all.",
                subtitleTamil = "அவர் உயிர்த்தெழுந்தார்! இயேசு மரணத்தை வென்று நித்திய ஜீவனைத் தந்தார்.",
                subtitleTelugu = "ఆయన లేచియున్నాడు! యేసు మరణాన్ని జయించి నిత్యజీవాన్ని ఇచ్చాడు.",
                imageUrl = "https://images.unsplash.com/photo-1438232992991-995b7058bbb3?w=800",
                category = "New Testament",
                badgeIcon = "heart",
                badgeColorHex = 0xFFFF4081,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "He is Risen Indeed!",
                        headlineTamil = "அவர் மெய்யாகவே உயிர்த்தெழுந்தார்!",
                        headlineTelugu = "ఆయన నిజముగా తిరిగి లేచాడు!",
                        textEnglish = "On Sunday morning, Mary went to the tomb and found the giant stone rolled away! An angel beamed: 'He is not here: for he is risen, as he said!' Jesus is alive forever!",
                        textTamil = "ஞாயிறு காலையில் பெரிய கல் புரட்டப்பட்டிருந்தது! தேவ தூதன், 'அவர் இங்கே இல்லை, தாம் சொன்னபடியே உயிர்த்தெழுந்தார்' என்றார். இயேசு என்றும் வாழ்கிறார்!",
                        textTelugu = "ఆదివారం ఉదయాన్నే రాయి దొర్లించబడి ఉంది! దేవదூత 'ఆయన ఇక్కడ లేడు, తాను చెప్పినట్లే తిరిగి లేచాడు' అని చెప్పాడు. యేసు నిరంతరం జీవిస్తున్నాడు!",
                        teacherNoteEnglish = "Teacher Grace says: Because Jesus is alive today, we have joy, hope, and everlasting life!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: இயேசு இன்று உயிரோடு இருப்பதால் நமக்கு நம்பிக்கை மற்றும் மகிழ்ச்சி உண்டு!",
                        teacherNoteTelugu = "టీచర్ గ్రేஸ் చెప్తున్నారు: యేసయ్య సజీవుడు కాబట్టి మనకు నిరీక్షణ, ఆనందం ఉన్నాయి!",
                        imageUrl = "https://images.unsplash.com/photo-1438232992991-995b7058bbb3?w=800",
                        memoryVerseEnglish = "Matthew 28:6 - He is not here: for he is risen, as he said.",
                        memoryVerseTamil = "மத்தேயு 28:6 - அவர் இங்கே இல்லை; தாம் சொன்னபடியே உயிர்த்தெழுந்தார்.",
                        memoryVerseTelugu = "మత్తయి 28:6 - ఆయన ఇక్కడ లేడు; తాను చెప్పినట్టే ఆయన లేచియున్నాడు."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "What joyful message did the angel share at the empty tomb?",
                        questionTamil = "கல்லறையில் வானதூதர்கள் அறிவித்த சந்தோஷ செய்தி என்ன?",
                        questionTelugu = "సమాధి వద్ద దేవదూత చెప్పిన శుభవార్త ఏమిటి?",
                        optionsEnglish = listOf("He is Risen!", "Go Home", "The stone is locked"),
                        optionsTamil = listOf("அவர் உயிர்த்தெழுந்தார்!", "வீட்டுக்குச் செல்லுங்கள்", "கல் பூட்டப்பட்டுள்ளது"),
                        optionsTelugu = listOf("ఆయన తిరిగి లేచియున్నాడు!", "ఇంటికి వెళ్లండి", "రాయి మూసివేసి ఉంది"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Hallelujah! Jesus is alive forevermore!",
                        teacherExplanationTamil = "அல்லேலூயா! இயேசு என்றென்றும் உயிரோடு இருக்கிறார்!",
                        teacherExplanationTelugu = "హల్లెలూయా! యేసు ఎల్లప్పుడూ జీవిస్తున్నాడు!"
                    )
                )
            )
    )
    */

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
