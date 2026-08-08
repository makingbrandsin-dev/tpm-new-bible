package com.example.data

import com.example.model.KidsStory
import com.example.model.KidsStoryPage
import com.example.model.QuizQuestion

object KidsCatalog {

    fun get100KidsStories(): List<KidsStory> {
        val list = mutableListOf<KidsStory>()

        val rawData = listOf(
            // 1..10
            Triple("creation", "1. Creation of the World", "Old Testament"),
            Triple("noahs_ark", "2. Noah's Ark & The Rainbow", "Old Testament"),
            Triple("tower_of_babel", "3. The Tower of Babel", "Old Testament"),
            Triple("abraham_call", "4. Abraham's Journey of Faith", "Old Testament"),
            Triple("isaac_promise", "5. God's Promise to Sarah & Isaac", "Old Testament"),
            Triple("jacobs_ladder", "6. Jacob's Dream & Heavenly Ladder", "Old Testament"),
            Triple("joseph_coat", "7. Joseph's Coat of Many Colors", "Old Testament"),
            Triple("joseph_egypt", "8. Joseph Saved Egypt from Famine", "Old Testament"),
            Triple("baby_moses", "9. Baby Moses in the Basket", "Old Testament"),
            Triple("burning_bush", "10. Moses & The Burning Bush", "Old Testament"),

            // 11..20
            Triple("red_sea", "11. Parting of the Red Sea", "Old Testament"),
            Triple("manna_rock", "12. Manna & Water from the Rock", "Old Testament"),
            Triple("ten_commandments", "13. The Ten Commandments", "Old Testament"),
            Triple("tabernacle", "14. God's Holy Tabernacle", "Old Testament"),
            Triple("joshua_jericho", "15. The Falling Walls of Jericho", "Old Testament"),
            Triple("gideon_300", "16. Gideon and the 300 Warriors", "Old Testament"),
            Triple("samson_strength", "17. Samson's Great Strength", "Old Testament"),
            Triple("ruth_naomi", "18. Ruth's Kindness & Loyalty", "Old Testament"),
            Triple("young_samuel", "19. Young Samuel Hears God's Voice", "Old Testament"),
            Triple("david_goliath", "20. David and Goliath", "Old Testament"),

            // 21..30
            Triple("david_jonathan", "21. David and Jonathan's Friendship", "Old Testament"),
            Triple("king_solomon_wisdom", "22. King Solomon's Great Wisdom", "Old Testament"),
            Triple("solomon_temple", "23. Building the Holy Temple", "Old Testament"),
            Triple("elijah_chariot", "24. Elijah & The Chariot of Fire", "Old Testament"),
            Triple("elisha_oil", "25. Elisha & The Widow's Oil", "Old Testament"),
            Triple("naaman_healed", "26. Naaman Healed in the Jordan", "Old Testament"),
            Triple("queen_esther", "27. Brave Queen Esther Saves Her People", "Old Testament"),
            Triple("nehemiah_walls", "28. Nehemiah Rebuilds Jerusalem Walls", "Old Testament"),
            Triple("daniel_lions", "29. Daniel in the Lions' Den", "Old Testament"),
            Triple("fiery_furnace", "30. Three Friends in the Fiery Furnace", "Old Testament"),

            // 31..40
            Triple("jonah_whale", "31. Jonah & The Big Fish", "Old Testament"),
            Triple("job_patience", "32. Job's Faith & Patience", "Old Testament"),
            Triple("hannah_prayer", "33. Hannah's Heartfelt Prayer", "Old Testament"),
            Triple("deborah_judge", "34. Deborah the Courageous Judge", "Old Testament"),
            Triple("hezekiah_prayer", "35. King Hezekiah's Miraculous Prayer", "Old Testament"),
            Triple("josiah_scroll", "36. Young King Josiah Finds God's Word", "Old Testament"),
            Triple("isaiah_vision", "37. Prophet Isaiah's Heavenly Vision", "Old Testament"),
            Triple("jeremiah_prophet", "38. Jeremiah Called as a Young Prophet", "Old Testament"),
            Triple("ezekiel_bones", "39. Ezekiel & The Valley of Dry Bones", "Old Testament"),
            Triple("malachi_promise", "40. Malachi's Promise of the Savior", "Old Testament"),

            // 41..50
            Triple("abigail_peace", "41. Abigail the Wise Peacemaker", "Old Testament"),
            Triple("gideon_fleece", "42. Gideon & The Wool Fleece", "Old Testament"),
            Triple("elisha_axhead", "43. Elisha Makes the Ax Head Float", "Old Testament"),
            Triple("queen_sheba", "44. The Queen of Sheba Visits Solomon", "Old Testament"),
            Triple("rebecca_well", "45. Rebekah at the Well", "Old Testament"),
            Triple("miriam_song", "46. Miriam's Victory Song & Tambourine", "Old Testament"),
            Triple("caleb_courage", "47. Caleb's Courageous Spirit", "Old Testament"),
            Triple("binaiah_lion", "48. Benaiah & The Lion in a Pit", "Old Testament"),
            Triple("mephibosheth", "49. David's Kindness to Mephibosheth", "Old Testament"),
            Triple("psalms_shepherd", "50. Psalm 23 - The Lord is My Shepherd", "Old Testament"),

            // 51..60
            Triple("angel_gabriel", "51. Angel Gabriel Visits Mary", "New Testament"),
            Triple("birth_of_jesus", "52. Jesus Born in Bethlehem", "New Testament"),
            Triple("shepherds_angels", "53. Shepherds & The Heavenly Angels", "New Testament"),
            Triple("wise_men_star", "54. Wise Men Follow the Bright Star", "New Testament"),
            Triple("boy_jesus_temple", "55. Boy Jesus in the Jerusalem Temple", "New Testament"),
            Triple("john_baptist", "56. John the Baptist Preaches Joy", "New Testament"),
            Triple("baptism_of_jesus", "57. The Baptism of Lord Jesus", "New Testament"),
            Triple("temptation_wilderness", "58. Jesus Overcomes Temptation", "New Testament"),
            Triple("calling_disciples", "59. Jesus Calls Fishermen Disciples", "New Testament"),
            Triple("wedding_cana", "60. Water Turned to Wine at Cana", "Miracles"),

            // 61..70
            Triple("nicodemus_night", "61. Jesus & Nicodemus Under the Stars", "New Testament"),
            Triple("samaritan_woman", "62. Woman at the Well of Water", "New Testament"),
            Triple("healing_officer_son", "63. Jesus Heals the Nobleman's Son", "Miracles"),
            Triple("paralytic_roof", "64. Paralytic Lowered Through the Roof", "Miracles"),
            Triple("healing_bethesda", "65. Healing at the Bethesda Pool", "Miracles"),
            Triple("calming_the_storm", "66. Jesus Calms the Raging Storm", "Miracles"),
            Triple("feeding_5000", "67. Feeding 5,000 with 5 Loaves & 2 Fish", "Miracles"),
            Triple("walking_on_water", "68. Jesus Walks on the Water", "Miracles"),
            Triple("healing_blind_man", "69. Blind Bartimaeus Receives Sight", "Miracles"),
            Triple("good_samaritan", "70. The Parable of the Good Samaritan", "New Testament"),

            // 71..80
            Triple("prodigal_son", "71. The Parable of the Prodigal Son", "New Testament"),
            Triple("lost_sheep", "72. The Shepherd Finds the Lost Sheep", "New Testament"),
            Triple("jairus_daughter", "73. Raising Jairus' Daughter", "Miracles"),
            Triple("ten_lepers", "74. Jesus Heals the Ten Lepers", "Miracles"),
            Triple("zacchaeus_tree", "75. Zacchaeus Climbs the Sycamore Tree", "New Testament"),
            Triple("raising_lazarus", "76. Jesus Raises Lazarus to Life", "Miracles"),
            Triple("jesus_blesses_children", "77. Jesus Blesses the Little Children", "New Testament"),
            Triple("widows_mite", "78. The Widow's Two Small Coins", "New Testament"),
            Triple("mustard_seed", "79. The Faith of a Tiny Mustard Seed", "New Testament"),
            Triple("sower_and_seed", "80. Parable of the Sower & Seed", "New Testament"),

            // 81..90
            Triple("palm_sunday", "81. Triumphal Entry on Palm Sunday", "New Testament"),
            Triple("washing_feet", "82. Jesus Washes the Disciples' Feet", "New Testament"),
            Triple("the_last_supper", "83. The Blessed Last Supper", "New Testament"),
            Triple("gethsemane_prayer", "84. Jesus Prays in Gethsemane", "New Testament"),
            Triple("easter_resurrection", "85. Jesus Rises from the Dead on Easter!", "New Testament"),
            Triple("mary_at_tomb", "86. Mary Magdalene at the Empty Tomb", "New Testament"),
            Triple("road_to_emmaus", "87. Walking on the Road to Emmaus", "New Testament"),
            Triple("doubting_thomas", "88. Thomas Sees the Risen Lord", "New Testament"),
            Triple("jesus_ascension", "89. Jesus Ascends into Heaven", "New Testament"),
            Triple("day_of_pentecost", "90. Holy Spirit Comes at Pentecost", "New Testament"),

            // 91..100
            Triple("peter_john_lame", "91. Peter & John Heal the Lame Man", "Miracles"),
            Triple("philip_ethiopian", "92. Philip & The Ethiopian Officer", "New Testament"),
            Triple("stephen_faith", "93. Stephen's Bright Faith", "New Testament"),
            Triple("saul_conversion", "94. Saul Transformed on Damascus Road", "New Testament"),
            Triple("dorcas_raised", "95. Kindness of Dorcas Brought to Life", "Miracles"),
            Triple("paul_silas_prison", "96. Paul & Silas Sing Praise in Prison", "New Testament"),
            Triple("paul_shipwreck", "97. Paul Survives the Maltese Shipwreck", "New Testament"),
            Triple("armor_of_god", "98. Putting on the Full Armor of God", "New Testament"),
            Triple("fruit_of_spirit", "99. The 9 Fruits of the Holy Spirit", "New Testament"),
            Triple("new_jerusalem", "100. John's Vision of the Heavenly New Jerusalem", "New Testament")
        )

        val sampleImages = listOf(
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800",
            "https://images.unsplash.com/photo-1519817650390-64a93db51149?w=800",
            "https://images.unsplash.com/photo-1509021436468-d5103e8b15d2?w=800",
            "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800",
            "https://images.unsplash.com/photo-1438232992991-995b7058bbb3?w=800",
            "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=800"
        )

        for ((index, item) in rawData.withIndex()) {
            val (id, title, cat) = item
            val img = sampleImages[index % sampleImages.size]
            val badgeColor = when (cat) {
                "Old Testament" -> 0xFFFFB300
                "Miracles" -> 0xFFE91E63
                else -> 0xFF4CAF50
            }

            val story = KidsStory(
                id = id,
                titleEnglish = title,
                titleTamil = "$title (தமிழ் கதைகள்)",
                titleTelugu = "$title (తెలుగు కథలు)",
                subtitleEnglish = "Inspiring Bible lesson $id for children filled with faith, courage, and love.",
                subtitleTamil = "விசுவாசம், தைரியம் மற்றும் அன்பால் நிறைந்த குழந்தைகளுக்கான விவிலியக் கதை.",
                subtitleTelugu = "విశ్వాసం, ధైర్యం మరియు ప్రేమతో నిండిన పిల్లల బైబిల్ పాఠం.",
                imageUrl = img,
                category = cat,
                badgeIcon = if (cat == "Miracles") "star" else "book",
                badgeColorHex = badgeColor,
                pages = listOf(
                    KidsStoryPage(
                        pageNumber = 1,
                        headlineEnglish = "$title - Part 1",
                        headlineTamil = "$title - பகுதி 1",
                        headlineTelugu = "$title - భాగం 1",
                        textEnglish = "Long ago, God showed His great power and love in this wonderful story! Children learned to trust Him with all their hearts.",
                        textTamil = "முற்காலத்தில், தேவன் தமது வல்லமையையும் அன்பையும் இந்த அற்புதக் கதையில் வெளிப்படுத்தினார்!",
                        textTelugu = "పూర్వకాలంలో, దేవుడు తన గొప్ప శక్తిని మరియు ప్రేమను ఈ అద్భుతమైన కథలో వ్యక్తపరిచాడు!",
                        teacherNoteEnglish = "Teacher Grace says: Remember children, God loves you dearly and is always watching over you!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: குழந்தைகளே, தேவன் உங்களை நேசிக்கிறார்!",
                        teacherNoteTelugu = "టీチャー గ్రేస్ చెప్తున్నారు: పిల్లలు, దేవుడు మిమ్మల్ని ప్రేమిస్తున్నాడు!",
                        imageUrl = img,
                        memoryVerseEnglish = "Psalm 119:105 - Thy word is a lamp unto my feet, and a light unto my path.",
                        memoryVerseTamil = "சங்கீதம் 119:105 - உம்முடைய வசனம் என் கால்களுக்குத் தீபமும், என் பாதைக்கு வெளிச்சமுமாயிருக்கிறது.",
                        memoryVerseTelugu = "కీர்తనలు 119:105 - నీ వాక్యము నా పాదములకు దీపమును నా త్రోవకు వెలుగునై యున్నది."
                    ),
                    KidsStoryPage(
                        pageNumber = 2,
                        headlineEnglish = "$title - God's Miracle & Blessing",
                        headlineTamil = "$title - தேவ ஆசீர்வாதம்",
                        headlineTelugu = "$title - దేవుని ఆశీర్వాదం",
                        textEnglish = "And God brought victory, peace, and joy! Whenever we pray with faith, God answers our prayers.",
                        textTamil = "தேவன் சமாதானத்தையும் மகிழ்ச்சியையும் தந்தார்! நாம் ஜெபிக்கும்போது தேவன் பதிலளிக்கிறார்.",
                        textTelugu = "దేవుడు సమాధానమును సంతోషమును ఇచ్చెను! మనం ప్రార్థించినప్పుడు దేవుడు జవాబిస్తాడు.",
                        teacherNoteEnglish = "Teacher Grace says: Keep praying every day and shining God's light to everyone around you!",
                        teacherNoteTamil = "ஆசிரியை கிரேஸ் கூறுகிறார்: தினமும் ஜெபித்து தேவ ஒளியைப் வீசுங்கள்!",
                        teacherNoteTelugu = "టీచర్ గ్రేస్ చెప్తున్నారు: ప్రతిరోజు ప్రార్థించండి!",
                        imageUrl = img,
                        memoryVerseEnglish = "1 Thessalonians 5:17 - Pray without ceasing.",
                        memoryVerseTamil = "1 தெசலோனிக்கேயர் 5:17 - இடைவிடாமல் ஜெபம் பண்ணுங்கள்.",
                        memoryVerseTelugu = "1 థెస్సలొనీకయులకు 5:17 - ఎడతెగక ప్రార్థన చేయుడి."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = 1,
                        questionEnglish = "What is the main blessing taught in this story?",
                        questionTamil = "இந்தக் கதையின் முக்கிய பாடம் என்ன?",
                        questionTelugu = "ఈ కథలోని ముఖ్యమైన పాఠం ఏమిటి?",
                        optionsEnglish = listOf("Trusting God", "Getting Angry", "Fearing Darkness"),
                        optionsTamil = listOf("தேவனை நம்புதல்", "கோபப்படுதல்", "பயப்படுதல்"),
                        optionsTelugu = listOf("దేవుడిని నమ్మడం", "కోపపడటం", "భయపడటం"),
                        correctOptionIndex = 0,
                        teacherExplanationEnglish = "Amen! Always trust in God with all your heart.",
                        teacherExplanationTamil = "ஆமென்! எப்போதும் தேவனை முழு இருதயத்தோடும் நம்புங்கள்.",
                        teacherExplanationTelugu = "ఆమేన్! దేవుడిని పూర్ణ హృదయంతో నమ్మండి."
                    )
                )
            )

            list.add(story)
        }

        return list
    }
}
