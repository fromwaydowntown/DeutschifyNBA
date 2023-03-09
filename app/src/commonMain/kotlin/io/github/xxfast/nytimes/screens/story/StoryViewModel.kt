package io.github.xxfast.nytimes.screens.story

import app.cash.molecule.RecompositionMode.Immediate
import app.cash.molecule.moleculeFlow
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.state
import io.github.xxfast.nytimes.api.DeutschifyWebService
import io.github.xxfast.nytimes.components.AudioPlayer
import io.github.xxfast.nytimes.components.createAudioPlayer
import io.github.xxfast.nytimes.data.HttpClient
import io.github.xxfast.nytimes.data.store
import io.github.xxfast.nytimes.navigation.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StoryViewModel(
  context: RouterContext,
  title: String
) : ViewModel() {
  private val eventsFlow: MutableSharedFlow<StoryEvent> = MutableSharedFlow(5)
  private val initialState: StoryState = context.state(StoryState(title, null, null, false)) { states.value }
  private val audioPlayer: AudioPlayer = createAudioPlayer()
  private val webService = DeutschifyWebService(HttpClient)

  val states: StateFlow<StoryState> by lazy {
    moleculeFlow(Immediate) {
      StoryDomain(title, initialState, eventsFlow, store, webService, audioPlayer)
    }.stateIn(this, SharingStarted.Lazily, initialState)
  }

  fun onRefresh() { launch { eventsFlow.emit(StoryEvent.Refresh) } }
  fun onSave() { launch { eventsFlow.emit(StoryEvent.Save) } }
  fun onPlayPause() { launch { eventsFlow.emit(StoryEvent.Play) } }
  fun onSeekTo(position: Long) { audioPlayer.seekTo(position) }

  // Expose currentPosition and duration from AudioPlayer
  val currentPosition: StateFlow<Long> = flow {
    while (true) {
      emit(audioPlayer.currentPosition)
      delay(500) // Update every 500ms
    }
  }.stateIn(this, SharingStarted.Eagerly, 0L)

  val duration: StateFlow<Long> = flowOf(audioPlayer.duration).stateIn(this, SharingStarted.Eagerly, 0L)
}