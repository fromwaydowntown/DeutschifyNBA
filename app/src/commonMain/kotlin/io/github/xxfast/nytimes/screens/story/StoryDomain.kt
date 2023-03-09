package io.github.xxfast.nytimes.screens.story

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.xxfast.kstore.KStore
import io.github.xxfast.nytimes.api.DeutschifyWebService
import io.github.xxfast.nytimes.components.AudioPlayer
import io.github.xxfast.nytimes.components.createAudioPlayer
import io.github.xxfast.nytimes.models.SavedArticles
import io.github.xxfast.nytimes.models.SummaryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

var selectedArticle: io.github.xxfast.nytimes.screens.summary.SummaryState? = null

@Composable
fun StoryDomain(
    title: String,
    initialState: StoryState,
    events: Flow<StoryEvent>,
    store: KStore<SavedArticles>,
    deutschifyWebService: DeutschifyWebService,
    audioPlayer: AudioPlayer
): StoryState {
    var article: SummaryState? by remember { mutableStateOf(initialState.article) }
    var isPlaying: Boolean by remember { mutableStateOf(false) }
    var refreshes: Int by remember { mutableStateOf(0) }
    var isSaved: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                StoryEvent.Refresh -> refreshes++
                StoryEvent.Save -> launch(Dispatchers.Unconfined) {
                    store.update { savedArticles ->
                        val currentArticle = article
                        if (currentArticle != null) {
                            isSaved = !isSaved
                            savedArticles
                        } else savedArticles
                    }
                }
                StoryEvent.Play -> {
                    if (isPlaying) {
                        audioPlayer.pause()
                    } else {
                        val audioData = getAudioData(deutschifyWebService, selectedArticle?.description ?: "")
                        audioPlayer.play(audioData)
                    }
                    isPlaying = !isPlaying
                }
            }
        }
    }

    return StoryState(
        title = title,
        article = article,
        isSaved = isSaved,
        isPlaying = isPlaying
    )
}

private val httpClient = HttpClient()

private suspend fun getAudioData(deutschifyWebService: DeutschifyWebService, text: String): ByteArray {
    return try {
        deutschifyWebService.generateAudio(text)
    } catch (e: Exception) {
        // Handle exceptions, such as network errors
        throw Exception("Error fetching audio data: ${e.message}")
    }
}
