package io.github.xxfast.nytimes.screens.news

import app.cash.molecule.RecompositionMode.Immediate
import app.cash.molecule.moleculeFlow
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.state
import io.github.xxfast.nytimes.api.DeutschifyWebService
import io.github.xxfast.nytimes.data.HttpClient
import io.github.xxfast.nytimes.data.store
import io.github.xxfast.nytimes.navigation.ViewModel
import io.github.xxfast.nytimes.screens.news.NewsEvent.Refresh
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(context: RouterContext) : ViewModel() {
  private val eventsFlow: MutableSharedFlow<NewsEvent> = MutableSharedFlow(5)
  private val initialState: NewsState = context.state(NewsState()) { states.value }
  private val webService = DeutschifyWebService(HttpClient)

  val states: StateFlow<NewsState> by lazy {
    moleculeFlow(Immediate) { NewsDomain(initialState, eventsFlow, webService, store) }
      .stateIn(this, SharingStarted.Lazily, initialState)
  }

  fun onRefresh() { launch { eventsFlow.emit(Refresh) } }
}
