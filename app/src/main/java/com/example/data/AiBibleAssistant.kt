package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiVerseResult(
    val book: String,
    val chapter: Int,
    val verse: Int,
    val reference: String,
    val text: String,
    val theme: String = ""
)

object AiBibleAssistant {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    // Curated instant fallback topics for offline or quick access
    val quickTopics = listOf(
        "Peace & Anxiety",
        "Strength & Courage",
        "Healing & Health",
        "Hope & Trust",
        "God's Love",
        "Forgiveness",
        "Wisdom & Guidance",
        "Comfort in Grief"
    )

    private val localDatabase = mapOf(
        "Peace & Anxiety" to listOf(
            AiVerseResult("Philippians", 4, 6, "Philippians 4:6-7 (KJV)", "Be careful for nothing; but in every thing by prayer and supplication with thanksgiving let your requests be made known unto God. And the peace of God, which passeth all understanding, shall keep your hearts and minds through Christ Jesus.", "Overcoming Worry"),
            AiVerseResult("John", 14, 27, "John 14:27 (KJV)", "Peace I leave with you, my peace I give unto you: not as the world giveth, give I unto you. Let not your heart be troubled, neither let it be afraid.", "Christ's Peace"),
            AiVerseResult("Isaiah", 26, 3, "Isaiah 26:3 (KJV)", "Thou wilt keep him in perfect peace, whose mind is stayed on thee: because he trusteth in thee.", "Perfect Peace"),
            AiVerseResult("Psalms", 55, 22, "Psalm 55:22 (KJV)", "Cast thy burden upon the LORD, and he shall sustain thee: he shall never suffer the righteous to be moved.", "Casting Burdens"),
            AiVerseResult("1 Peter", 5, 7, "1 Peter 5:7 (KJV)", "Casting all your care upon him; for he careth for you.", "God's Care")
        ),
        "Strength & Courage" to listOf(
            AiVerseResult("Isaiah", 40, 31, "Isaiah 40:31 (KJV)", "But they that wait upon the LORD shall renew their strength; they shall mount up with wings as eagles; they shall run, and not be weary; and they shall walk, and not faint.", "Renewed Strength"),
            AiVerseResult("Joshua", 1, 9, "Joshua 1:9 (KJV)", "Have not I commanded thee? Be strong and of a good courage; be not afraid, neither be thou dismayed: for the LORD thy God is with thee whithersoever thou goest.", "Courageous Faith"),
            AiVerseResult("Psalms", 46, 1, "Psalm 46:1 (KJV)", "God is our refuge and strength, a very present help in trouble.", "Refuge & Help"),
            AiVerseResult("Philippians", 4, 13, "Philippians 4:13 (KJV)", "I can do all things through Christ which strengtheneth me.", "Empowered in Christ"),
            AiVerseResult("2 Corinthians", 12, 9, "2 Corinthians 12:9 (KJV)", "And he said unto me, My grace is sufficient for thee: for my strength is made perfect in weakness.", "Grace in Weakness")
        ),
        "Healing & Health" to listOf(
            AiVerseResult("Jeremiah", 17, 14, "Jeremiah 17:14 (KJV)", "Heal me, O LORD, and I shall be healed; save me, and I shall be saved: for thou art my praise.", "Prayer for Healing"),
            AiVerseResult("Psalms", 103, 2, "Psalm 103:2-3 (KJV)", "Bless the LORD, O my soul, and forget not all his benefits: Who forgiveth all thine iniquities; who healeth all thy diseases.", "God Heals All Diseases"),
            AiVerseResult("Exodus", 15, 26, "Exodus 15:26 (KJV)", "For I am the LORD that healeth thee.", "Jehovah Rapha"),
            AiVerseResult("James", 5, 15, "James 5:15 (KJV)", "And the prayer of faith shall save the sick, and the Lord shall raise him up; and if he have committed sins, they shall be forgiven him.", "Prayer of Faith"),
            AiVerseResult("Isaiah", 53, 5, "Isaiah 53:5 (KJV)", "But he was wounded for our transgressions, he was bruised for our iniquities: the chastisement of our peace was upon him; and with his stripes we are healed.", "Healed by His Stripes")
        ),
        "Hope & Trust" to listOf(
            AiVerseResult("Jeremiah", 29, 11, "Jeremiah 29:11 (KJV)", "For I know the thoughts that I think toward you, saith the LORD, thoughts of peace, and not of evil, to give you an expected end.", "God's Hopeful Plan"),
            AiVerseResult("Proverbs", 3, 5, "Proverbs 3:5-6 (KJV)", "Trust in the LORD with all thine heart; and lean not unto thine own understanding. In all thy ways acknowledge him, and he shall direct thy paths.", "Trusting God Completely"),
            AiVerseResult("Romans", 15, 13, "Romans 15:13 (KJV)", "Now the God of hope fill you with all joy and peace in believing, that ye may abound in hope, through the power of the Holy Ghost.", "Abounding in Hope"),
            AiVerseResult("Psalms", 37, 5, "Psalm 37:5 (KJV)", "Commit thy way unto the LORD; trust also in him; and he shall bring it to pass.", "Committing Your Way"),
            AiVerseResult("Hebrews", 11, 1, "Hebrews 11:1 (KJV)", "Now faith is the substance of things hoped for, the evidence of things not seen.", "Nature of Faith & Hope")
        ),
        "God's Love" to listOf(
            AiVerseResult("John", 3, 16, "John 3:16 (KJV)", "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.", "The Gift of Salvation"),
            AiVerseResult("Romans", 8, 38, "Romans 8:38-39 (KJV)", "For I am persuaded, that neither death, nor life... shall be able to separate us from the love of God, which is in Christ Jesus our Lord.", "Unseparable Love"),
            AiVerseResult("1 John", 4, 19, "1 John 4:19 (KJV)", "We love him, because he first loved us.", "First Loved"),
            AiVerseResult("Psalms", 136, 1, "Psalm 136:1 (KJV)", "O give thanks unto the LORD; for he is good: for his mercy endureth for ever.", "Enduring Mercy"),
            AiVerseResult("Zephaniah", 3, 17, "Zephaniah 3:17 (KJV)", "The LORD thy God in the midst of thee is mighty; he will save, he will rejoice over thee with joy; he will rest in his love.", "Resting in His Love")
        ),
        "Forgiveness" to listOf(
            AiVerseResult("1 John", 1, 9, "1 John 1:9 (KJV)", "If we confess our sins, he is faithful and just to forgive us our sins, and to cleanse us from all unrighteousness.", "Faithful to Forgive"),
            AiVerseResult("Ephesians", 4, 32, "Ephesians 4:32 (KJV)", "And be ye kind one to another, tenderhearted, forgiving one another, even as God for Christ's sake hath forgiven you.", "Forgiving Others"),
            AiVerseResult("Psalms", 103, 12, "Psalm 103:12 (KJV)", "As far as the east is from the west, so far hath he removed our transgressions from us.", "Removal of Transgressions"),
            AiVerseResult("Colossians", 3, 13, "Colossians 3:13 (KJV)", "Forbearing one another, and forgiving one another, if any man have a quarrel against any: even as Christ forgave you, so also do ye.", "Christ's Example"),
            AiVerseResult("Isaiah", 1, 18, "Isaiah 1:18 (KJV)", "Come now, and let us reason together, saith the LORD: though your sins be as scarlet, they shall be as white as snow.", "Cleansed White as Snow")
        ),
        "Wisdom & Guidance" to listOf(
            AiVerseResult("James", 1, 5, "James 1:5 (KJV)", "If any of you lack wisdom, let him ask of God, that giveth to all men liberally, and upbraideth not; and it shall be given him.", "Asking for Wisdom"),
            AiVerseResult("Psalms", 119, 105, "Psalm 119:105 (KJV)", "Thy word is a lamp unto my feet, and a light unto my path.", "Lamp & Light"),
            AiVerseResult("Proverbs", 16, 3, "Proverbs 16:3 (KJV)", "Commit thy works unto the LORD, and thy thoughts shall be established.", "Establishing Thoughts"),
            AiVerseResult("Psalms", 32, 8, "Psalm 32:8 (KJV)", "I will instruct thee and teach thee in the way which thou shalt go: I will guide thee with mine eye.", "Divine Instruction"),
            AiVerseResult("Proverbs", 4, 7, "Proverbs 4:7 (KJV)", "Wisdom is the principal thing; therefore get wisdom: and with all thy getting get understanding.", "The Principal Thing")
        ),
        "Comfort in Grief" to listOf(
            AiVerseResult("Psalms", 34, 18, "Psalm 34:18 (KJV)", "The LORD is nigh unto them that are of a broken heart; and saveth such as be of a contrite spirit.", "Near to Brokenhearted"),
            AiVerseResult("Matthew", 5, 4, "Matthew 5:4 (KJV)", "Blessed are they that mourn: for they shall be comforted.", "Promise to Mourners"),
            AiVerseResult("2 Corinthians", 1, 3, "2 Corinthians 1:3-4 (KJV)", "Blessed be God... the Father of mercies, and the God of all comfort; Who comforteth us in all our tribulation.", "God of All Comfort"),
            AiVerseResult("Psalms", 23, 4, "Psalm 23:4 (KJV)", "Yea, though I walk through the valley of the shadow of death, I will fear no evil: for thou art with me; thy rod and thy staff they comfort me.", "Valley of Shadow"),
            AiVerseResult("Revelation", 21, 4, "Revelation 21:4 (KJV)", "And God shall wipe away all tears from their eyes; and there shall be no more death, neither sorrow, nor crying.", "No More Tears")
        )
    )

    suspend fun findVersesForTopic(topic: String): List<AiVerseResult> = withContext(Dispatchers.IO) {
        val trimmed = topic.trim()
        if (trimmed.isEmpty()) return@withContext emptyList()

        // Match local database first if exact or partial match
        localDatabase.keys.firstOrNull { it.contains(trimmed, ignoreCase = true) || trimmed.contains(it, ignoreCase = true) }?.let { matchedKey ->
            localDatabase[matchedKey]?.let { return@withContext it }
        }

        // Call Gemini API (gemini-3.5-flash)
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Fallback to searching local database keywords if key not set
            return@withContext searchLocalByKeyword(trimmed)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val prompt = """
                You are a King James Version (KJV) Bible verse search assistant.
                The user wants to find KJV Bible verses for the topic: "$trimmed".
                Provide 4 to 6 relevant KJV Bible verses matching this topic.
                Respond strictly in raw JSON array format with no markdown wrappers or backticks.
                Each item in the JSON array must be an object with these exact keys:
                - "book": Name of book (e.g. "Psalms", "John", "1 Corinthians", "Isaiah")
                - "chapter": Chapter number as integer
                - "verse": Verse number as integer
                - "reference": Full reference string (e.g. "Psalm 23:1 (KJV)")
                - "text": Exact KJV verse text
                - "theme": Short 2-4 word subtheme/tag (e.g. "Comfort in Darkness")
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseText = response.body?.string() ?: ""

            if (response.isSuccessful && responseText.isNotEmpty()) {
                val parsed = parseGeminiResponse(responseText)
                if (parsed.isNotEmpty()) return@withContext parsed
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback search local database if API fails or network unavailable
        return@withContext searchLocalByKeyword(trimmed)
    }

    private fun parseGeminiResponse(jsonString: String): List<AiVerseResult> {
        val list = mutableListOf<AiVerseResult>()
        try {
            val rootObj = JSONObject(jsonString)
            val candidates = rootObj.optJSONArray("candidates") ?: return list
            if (candidates.length() == 0) return list
            val candidate = candidates.getJSONObject(0)
            val content = candidate.optJSONObject("content") ?: return list
            val parts = content.optJSONArray("parts") ?: return list
            if (parts.length() == 0) return list
            var text = parts.getJSONObject(0).optString("text", "")

            // Clean markdown code blocks if any
            text = text.trim()
            if (text.startsWith("```json")) {
                text = text.substring(7)
            } else if (text.startsWith("```")) {
                text = text.substring(3)
            }
            if (text.endsWith("```")) {
                text = text.substring(0, text.length - 3)
            }
            text = text.trim()

            val jsonArray = JSONArray(text)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                list.add(
                    AiVerseResult(
                        book = item.optString("book", "John"),
                        chapter = item.optInt("chapter", 1),
                        verse = item.optInt("verse", 1),
                        reference = item.optString("reference", ""),
                        text = item.optString("text", ""),
                        theme = item.optString("theme", "Scripture")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun searchLocalByKeyword(keyword: String): List<AiVerseResult> {
        val lower = keyword.lowercase()
        val allLocal = localDatabase.values.flatten()
        val matched = allLocal.filter {
            it.text.lowercase().contains(lower) ||
                    it.reference.lowercase().contains(lower) ||
                    it.theme.lowercase().contains(lower)
        }
        return if (matched.isNotEmpty()) matched else localDatabase.values.first()
    }
}
