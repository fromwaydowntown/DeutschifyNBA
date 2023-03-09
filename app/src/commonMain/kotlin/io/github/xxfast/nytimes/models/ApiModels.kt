package io.github.xxfast.nytimes.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class TopStorySection(val name: String)

@Serializable
data class GetArticlesResponse(
  val articles: List<SummaryState>,
)

@JvmInline
@Serializable
value class ArticleUri(val value: String): CharSequence by value
