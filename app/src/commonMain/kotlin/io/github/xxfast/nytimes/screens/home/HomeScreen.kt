package clutchapps.deutschify.screens.home

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.stack.RoutedContent
import io.github.xxfast.decompose.router.stack.Router
import io.github.xxfast.decompose.router.stack.rememberRouter
import clutchapps.deutschify.screens.home.StoryHomeScreen.Details
import clutchapps.deutschify.screens.home.StoryHomeScreen.List
import clutchapps.deutschify.screens.story.StoryScreen
import clutchapps.deutschify.screens.news.NewsScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun HomeScreen() {
  val router: Router<StoryHomeScreen> = rememberRouter(StoryHomeScreen::class) { listOf(List) }

  RoutedContent(
    router = router,
    animation = predictiveBackAnimation(
      backHandler = LocalRouterContext.current.backHandler,
      onBack = { router.pop() },
      fallbackAnimation = stackAnimation(slide())
    ),
  ) { screen ->
    when (screen) {
      List -> NewsScreen(
        onSelectArticle = { section, uri, title ->
          router.push(Details(section, title, uri))
        }
      )

      is Details -> StoryScreen(
        title = screen.title,
        description = screen.description,
        imageUrl = screen.image_url,
        onBack = { router.pop() },
      )
    }
  }
}

