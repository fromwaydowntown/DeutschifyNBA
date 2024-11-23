package clutchapps.deutschify.screens.story

import clutchapps.deutschify.models.SummaryState
import kotlinx.serialization.Serializable

val Loading: Nothing? = null
val DontKnowYet: Nothing? = null

@Serializable
data class StoryState(
  val title: String,
  // We can save the whole model in state here because we can fit it in state
  val article: SummaryState? = null,
  val isSaved: Boolean? = DontKnowYet,
  val isPlaying: Boolean,
)

sealed interface StoryEvent {
  data object Refresh : StoryEvent
  data object Save : StoryEvent
  data object Play : StoryEvent

  }


