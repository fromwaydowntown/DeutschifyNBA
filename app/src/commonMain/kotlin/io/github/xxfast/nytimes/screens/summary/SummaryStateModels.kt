package io.github.xxfast.nytimes.screens.summary

import io.github.xxfast.nytimes.models.SummaryState
import io.github.xxfast.nytimes.models.ArticleUri
import io.github.xxfast.nytimes.models.TopStorySection
import kotlinx.serialization.Serializable

@Serializable
data class SummaryState(
  val uri: ArticleUri,
  val imageUrl: String?,
  val title: String,
  val description: String,
  val section: TopStorySection? = null,
  val byline: String,
) {
  constructor(article: SummaryState) : this(
    uri = ArticleUri(""),
    imageUrl = article.image_url,
    title = article.adapted_title,
    description = article.adapted_teaser,
    section = null,
    byline = article.adapted_teaser,
  )
}


