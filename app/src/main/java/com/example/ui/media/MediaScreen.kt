package com.example.ui.media

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.model.MediaItem
import com.example.ui.BibleViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class BibleYouTubeVideo(
    val id: String,
    val title: String,
    val category: String, // "Bible Video", "Christian Song", "Sermon"
    val channelOrArtist: String,
    val duration: String,
    val youtubeVideoId: String,
    val thumbnailUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val initialMediaList = remember { viewModel.repository.getSampleMedia() }

    var customLocalMediaList by remember { mutableStateOf<List<MediaItem>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchingInternet by remember { mutableStateOf(false) }

    // Mobile Device Audio Picker Launcher
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val newItems = uris.mapIndexed { index, uri ->
                val fileName = uri.lastPathSegment?.substringAfterLast('/') ?: "Local Track ${index + 1}"
                MediaItem(
                    id = "local_${System.currentTimeMillis()}_$index",
                    title = fileName.replace(".mp3", "").replace(".m4a", "").replace("_", " "),
                    speakerOrArtist = "Device Mobile Storage",
                    audioUrl = uri.toString(),
                    thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&q=80",
                    duration = "Mobile File",
                    category = "Local Audio"
                )
            }
            customLocalMediaList = customLocalMediaList + newItems
            newItems.firstOrNull()?.let { firstItem ->
                viewModel.playMediaItem(firstItem)
            }
        }
    }

    val bibleVideos = remember {
        listOf(
            BibleYouTubeVideo(
                id = "v1",
                title = "The Gospel of John - Life of Jesus Christ",
                category = "Bible Video",
                channelOrArtist = "Lumo Project • Bible Videos",
                duration = "15:20",
                youtubeVideoId = "73J1TqC8s4c",
                thumbnailUrl = "https://images.unsplash.com/photo-1509021436468-d5103e8b15d2?w=800&q=80"
            ),
            BibleYouTubeVideo(
                id = "v2",
                title = "David and Goliath - The Faith of a Shepherd Boy",
                category = "Bible Video",
                channelOrArtist = "Bible Animated Classics",
                duration = "12:45",
                youtubeVideoId = "N_Gq0f6MlsA",
                thumbnailUrl = "https://images.unsplash.com/photo-1519817650390-64a93db51149?w=800&q=80"
            ),
            BibleYouTubeVideo(
                id = "v3",
                title = "How Great Thou Art - TPM Worship Choir",
                category = "Christian Song",
                channelOrArtist = "TPM Bollarum Choir",
                duration = "05:30",
                youtubeVideoId = "3G4nkbdlMMe",
                thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&q=80"
            ),
            BibleYouTubeVideo(
                id = "v4",
                title = "Jesus Calms the Storm - Gospel According to Mark",
                category = "Bible Video",
                channelOrArtist = "Bible Teaching Series",
                duration = "08:15",
                youtubeVideoId = "_x1bKlhC8o4",
                thumbnailUrl = "https://images.unsplash.com/photo-1438232992991-995b7058bbb3?w=800&q=80"
            ),
            BibleYouTubeVideo(
                id = "v5",
                title = "Blessed Assurance - Christian Praise Song",
                category = "Christian Song",
                channelOrArtist = "Grace Worship Medley",
                duration = "04:45",
                youtubeVideoId = "m0e3A1lK4D8",
                thumbnailUrl = "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=800&q=80"
            )
        )
    }

    var selectedVideo by remember { mutableStateOf(bibleVideos.first()) }
    var selectedCategoryFilter by remember { mutableStateOf("All") }

    val filteredVideos = remember(selectedCategoryFilter, searchQuery) {
        val base = when (selectedCategoryFilter) {
            "Bible Videos" -> bibleVideos.filter { it.category == "Bible Video" }
            "Christian Songs" -> bibleVideos.filter { it.category == "Christian Song" }
            else -> bibleVideos
        }
        if (searchQuery.isBlank()) base
        else base.filter { it.title.contains(searchQuery, ignoreCase = true) || it.channelOrArtist.contains(searchQuery, ignoreCase = true) }
    }

    val filteredAudioList = remember(initialMediaList, customLocalMediaList, searchQuery, selectedCategoryFilter) {
        val base = customLocalMediaList + initialMediaList
        val categoryFiltered = when (selectedCategoryFilter) {
            "Sermons" -> base.filter { it.category.equals("Sermons", ignoreCase = true) }
            "Local Audio" -> base.filter { it.category.equals("Local Audio", ignoreCase = true) }
            else -> base
        }
        if (searchQuery.isBlank()) categoryFiltered
        else categoryFiltered.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.speakerOrArtist.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("media_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        // 1. Internet Search Bar & Import Local Phone Storage Button
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    if (query.isNotBlank()) {
                        scope.launch {
                            isSearchingInternet = true
                            delay(350)
                            isSearchingInternet = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("media_search_input"),
                placeholder = { Text("Search internet songs, videos, psalms & sermons...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = CelestialGold
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = OnSurfaceVariantMuted
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CelestialGold,
                    unfocusedBorderColor = MutedOutlineVariant
                )
            )

            // Add Mobile Device Audio Files Button
            Button(
                onClick = { audioPickerLauncher.launch("audio/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("import_mobile_audio_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceContainerHigh,
                    contentColor = CelestialGold
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialGold.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Default.FolderOpen,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Import Local Audio from Mobile Storage 📱",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Search Loading Indicator
        AnimatedVisibility(visible = isSearchingInternet) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CelestialGold.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = CelestialGold,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Searching online media repository...",
                        fontSize = 12.sp,
                        color = CelestialGold
                    )
                }
            }
        }

        // 2. Embedded Active YouTube Video Player
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(Color.Black)
                ) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.mediaPlaybackRequiresUserGesture = false
                                webViewClient = WebViewClient()
                                loadUrl("https://www.youtube.com/embed/${selectedVideo.youtubeVideoId}?autoplay=0&controls=1")
                            }
                        },
                        update = { webView ->
                            webView.loadUrl("https://www.youtube.com/embed/${selectedVideo.youtubeVideoId}?autoplay=0&controls=1")
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(modifier = Modifier.padding(14.dp)) {
                    Surface(
                        color = if (selectedVideo.category == "Bible Video") CelestialGold.copy(alpha = 0.15f) else KidsPink.copy(alpha = 0.15f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = selectedVideo.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedVideo.category == "Bible Video") CelestialGold else KidsPink,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = selectedVideo.title,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = "${selectedVideo.channelOrArtist} • ${selectedVideo.duration}",
                        fontSize = 12.sp,
                        color = OnSurfaceVariantMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Category Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All", "Bible Videos", "Christian Songs", "Sermons", "Local Audio").forEach { cat ->
                FilterChip(
                    selected = selectedCategoryFilter == cat,
                    onClick = { selectedCategoryFilter = cat },
                    label = { Text(cat, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CelestialGold,
                        selectedLabelColor = OnPrimaryDark,
                        containerColor = SurfaceContainer,
                        labelColor = OnSurfaceLight
                    )
                )
            }
        }

        // 3. YouTube Video Playlist Section
        if (filteredVideos.isNotEmpty()) {
            Text(
                text = "Featured Videos (${filteredVideos.size})",
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceLight
            )

            filteredVideos.forEach { video ->
                val isPlayingThis = video.id == selectedVideo.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedVideo = video }
                        .testTag("video_item_${video.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPlayingThis) SurfaceContainerHigh else SurfaceContainer
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isPlayingThis) CelestialGold else Color.Transparent
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 90.dp, height = 60.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            AsyncImage(
                                model = video.thumbnailUrl,
                                contentDescription = video.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(SurfaceDark.copy(alpha = 0.35f))
                            )

                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPlayingThis) CelestialGold else OnSurfaceLight,
                                maxLines = 2
                            )
                            Text(
                                text = "${video.channelOrArtist} • ${video.duration}",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Audio Sermons, Hymns & Local Files Section
        if (filteredAudioList.isNotEmpty()) {
            Text(
                text = "Audio Sermons, Hymns & Local Tracks (${filteredAudioList.size})",
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceLight,
                modifier = Modifier.padding(top = 8.dp)
            )

            filteredAudioList.forEach { media ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.playMediaItem(media) }
                        .testTag("media_item_${media.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CelestialGold)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = OnPrimaryDark,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = media.category.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CelestialGold
                            )
                            Text(
                                text = media.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceLight
                            )
                            Text(
                                text = "${media.speakerOrArtist} • ${media.duration}",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
