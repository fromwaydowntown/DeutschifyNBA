package clutchapps.deutschify.screens.summary

import clutchapps.deutschify.models.SummaryState
import clutchapps.deutschify.models.ArticleUri
import clutchapps.deutschify.models.TopStorySection
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


