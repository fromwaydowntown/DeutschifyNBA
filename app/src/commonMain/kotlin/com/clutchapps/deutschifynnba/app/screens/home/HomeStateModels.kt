package clutchapps.deutschify.screens.home

import clutchapps.deutschify.models.ArticleUri
import clutchapps.deutschify.models.TopStorySection
import kotlinx.serialization.Serializable

@Serializable
sealed class StoryHomeScreen {
  @Serializable
  data object List : StoryHomeScreen()

  @Serializable
  data class Details(
    val title: String,
    val description: String,
    val image_url: String
  ) : StoryHomeScreen()
}
