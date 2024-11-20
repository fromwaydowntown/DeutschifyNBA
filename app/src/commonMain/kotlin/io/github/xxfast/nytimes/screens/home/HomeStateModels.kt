package io.github.xxfast.nytimes.screens.home

import io.github.xxfast.nytimes.models.ArticleUri
import io.github.xxfast.nytimes.models.TopStorySection
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
