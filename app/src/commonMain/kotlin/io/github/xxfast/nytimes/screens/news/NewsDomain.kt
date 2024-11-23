package clutchapps.deutschify.screens.news

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.xxfast.kstore.KStore
import clutchapps.deutschify.api.DeutschifyWebService
import clutchapps.deutschify.models.SavedArticles
import clutchapps.deutschify.models.GetArticlesResponse
import clutchapps.deutschify.models.TopStorySection
import clutchapps.deutschify.screens.summary.SummaryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
fun NewsDomain(
  initialState: NewsState,
  events: Flow<NewsEvent>,
  webService: DeutschifyWebService,
  store: KStore<SavedArticles>,
): NewsState {
  var section: TopStorySection? by remember { mutableStateOf(initialState.section) }
  var articles: List<SummaryState>? by remember { mutableStateOf(initialState.articles) }

  val favourites: List<SummaryState>? by store.updates
    .map{ savedArticles -> savedArticles.orEmpty().map(::SummaryState) }
    .collectAsState(Loading)

  var refreshes: Int by remember { mutableStateOf(0) }
  val numberOfFavourites: Int? = favourites?.size

  LaunchedEffect(refreshes) {
    // Don't autoload the stories when restored from process death
    if (refreshes == 0 && articles != Loading) return@LaunchedEffect

    articles = Loading

    val topStory: GetArticlesResponse = webService.getArticles()

    articles = topStory.articles.map(::SummaryState)
  }

  LaunchedEffect(Unit) {
    events.collect { event ->
      when (event) {
        NewsEvent.Refresh -> refreshes++

        is NewsEvent.SelectSection -> {
          // reset the section to home if it is already selected
          section = if (event.section == section) null else event.section
          refreshes++
        }
      }
    }
  }

  return NewsState(section, articles, numberOfFavourites)
}
