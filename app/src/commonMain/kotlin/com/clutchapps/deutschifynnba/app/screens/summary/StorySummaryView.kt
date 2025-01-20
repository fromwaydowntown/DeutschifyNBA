package clutchapps.deutschify.screens.summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import clutchapps.deutschify.models.ArticleUri
import clutchapps.deutschify.models.TopStorySection
import clutchapps.deutschify.screens.story.ArticleImage

@Composable
fun StorySummaryView(
  summary: SummaryState,
  isSelected: Boolean,
  onSelect: (title: String, description: String, image: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    shape = MaterialTheme.shapes.extraLarge,
    tonalElevation = if (isSelected) 2.dp else 0.dp,
    modifier = modifier
      .clip(MaterialTheme.shapes.extraLarge)
      .clickable { onSelect(summary.title, summary.description, summary.imageUrl ?: "") }
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.padding(8.dp)
    ) {
      if (summary.imageUrl != null) Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .clip(MaterialTheme.shapes.extraLarge)
      ) {
        ArticleImage(summary.imageUrl)
      }

      Text(
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 2.dp, end = 2.dp),
        text = summary.title,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.headlineSmall,
      )

      Text(
        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp, start = 2.dp, end = 2.dp),
        text = summary.description,
        style = MaterialTheme.typography.labelMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}
