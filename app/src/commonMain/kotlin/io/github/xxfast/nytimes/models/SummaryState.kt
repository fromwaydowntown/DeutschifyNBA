package clutchapps.deutschify.models

import kotlinx.serialization.Serializable

@Serializable
data class SummaryState(
    val title: String,
    val adapted_title: String,
    val image_url: String,
    val adapted_teaser: String,
    val published_date: String,
    val multimedia: List<MultiMedia>? =null
)