package clutchapps.deutschify.screens.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xxfast.decompose.router.rememberOnRoute
import clutchapps.deutschify.components.TwoPanelScaffold
import clutchapps.deutschify.components.TwoPanelScaffoldAnimationSpec
import clutchapps.deutschify.models.ArticleUri
import clutchapps.deutschify.screens.home.StoryHomeScreen
import clutchapps.deutschify.screens.story.StoryScreen
import clutchapps.deutschify.screens.story.selectedArticle
import clutchapps.deutschify.screens.summary.StorySummaryView
import clutchapps.deutschify.utils.statusBarPadding

@Composable
fun NewsScreen(
  onSelectArticle: (title: String, image: String, description: String) -> Unit,
) {
  val viewModel: NewsViewModel =
    rememberOnRoute(NewsViewModel::class) { savedState -> NewsViewModel(savedState) }

  val state: NewsState by viewModel.states.collectAsState()

  var selection: StoryHomeScreen? by remember { mutableStateOf(null) }
  var article: clutchapps.deutschify.screens.summary.SummaryState? by remember {
    mutableStateOf(
      null
    )
  }
  val details: StoryHomeScreen.Details? = selection as? StoryHomeScreen.Details
  val windowSizeClass: WindowSizeClass = com.clutchapps.deutschifynnba.androidx.compose.material3.windowsizeclass.LocalWindowSizeClass.current
  var showPanel: Boolean by remember { mutableStateOf(details != null) }

  // Reset selection if the window size class changes to compact
  LaunchedEffect(windowSizeClass) {
    selection = selection.takeIf { windowSizeClass.widthSizeClass != Compact }
    showPanel = selection != null
  }

  TwoPanelScaffold(
    panelVisibility = showPanel,
    animationSpec = TwoPanelScaffoldAnimationSpec(
      finishedListener = { fraction -> if (fraction == 1f) selection = null }
    ),
    body = {
      NewsView(
        state = state,
        selected = ArticleUri(details?.title ?: ""),
        onSelect = { title, description, image ->
          val next = StoryHomeScreen.Details(title, description, image)
          article = state.articles?.findLast { it.title == title }
          selectedArticle = article
          if (windowSizeClass.widthSizeClass == Compact) {
            onSelectArticle(
              article?.title ?: "",
              article?.imageUrl ?: "",
              article?.description ?: ""
            )
            return@NewsView
          }

          selection = next
          showPanel = true
        },
        onRefresh = viewModel::onRefresh
      )
    },
    panel = {
      Surface(tonalElevation = 1.dp) {
        if (details != null) StoryScreen(
          title = article?.title ?: "",
          onBack = { showPanel = false },
          onFullScreen = {
            onSelectArticle(
              article?.title ?: "",
              article?.imageUrl ?: "",
              article?.description ?: ""
            )
          },
          description = article?.description ?: "",
          imageUrl = article?.imageUrl ?: "",
        )
      }
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewsView(
  state: NewsState,
  selected: ArticleUri?,
  onRefresh: () -> Unit,
  onSelect: (title: String, description: String, image: String) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text("NBA news")
        },
        actions = {
          IconButton(onClick = onRefresh) {
            Icon(Icons.Rounded.Refresh, contentDescription = null)
          }
        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBarPadding)
      )
    },
    modifier = modifier
  ) { scaffoldPadding ->
    Column(
      modifier = Modifier
        .scrollable(rememberScrollState(), Orientation.Vertical)
        .fillMaxSize()
        .padding(scaffoldPadding)
    ) {
      if (state.articles != Loading) {
        LazyVerticalGrid(
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          contentPadding = PaddingValues(16.dp),
          columns = GridCells.Adaptive(248.dp),
        ) {
          items(state.articles, key = { it.title }) { article ->
            StorySummaryView(
              summary = article,
              isSelected = article.uri == selected,
              onSelect = onSelect,
            )
          }

        }
      }

      AnimatedVisibility(
        visible = state.articles == Loading,
        enter = fadeIn(),
        exit = fadeOut(),
      ) {
        Box(modifier = Modifier.fillMaxSize()) {
          CircularProgressIndicator(modifier = Modifier.align(Center))
        }
      }
    }
  }
}

