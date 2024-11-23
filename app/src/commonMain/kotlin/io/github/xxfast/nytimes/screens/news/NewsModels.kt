package clutchapps.deutschify.screens.news

import clutchapps.deutschify.models.TopStorySection
import clutchapps.deutschify.screens.summary.SummaryState
import kotlinx.serialization.Serializable

val Loading: Nothing? = null

@Serializable
data class NewsState(
  val section: TopStorySection? = null,
  val articles: List<SummaryState>? = Loading,
  val numberOfFavourites: Int? = Loading,
)

sealed interface NewsEvent {
  data object Refresh: NewsEvent
  data class SelectSection(val section: TopStorySection): NewsEvent
}
