package clutchapps.deutschify.screens.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.OpenInFull
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import io.github.xxfast.decompose.router.rememberOnRoute
import clutchapps.deutschify.utils.statusBarPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
  title: String,
  description: String,
  imageUrl: String,
  onBack: () -> Unit,
  onFullScreen: (() -> Unit)? = null,
) {
  val viewModel: StoryViewModel = rememberOnRoute(StoryViewModel::class) { savedState -> 
    StoryViewModel(savedState, title)
  }

  val state by viewModel.states.collectAsState()
  val currentPosition by viewModel.currentPosition.collectAsState()
  val duration by viewModel.duration.collectAsState()

  val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  Surface(
    color = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxSize()
  ) {
    Scaffold(
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = MaterialTheme.colorScheme.onBackground,
      topBar = {
        TopAppBar(
          title = {
            AudioPlayerControls(
              isPlaying = state.isPlaying,
              currentPosition = currentPosition,
              duration = duration,
              onPlayPause = { viewModel.onPlayPause() },
              onSeekTo = { viewModel.onSeekTo(it) }
            )
          },
          navigationIcon = {
            IconButton(onClick = onBack) {
              Icon(
                Icons.AutoMirrored.Rounded.ArrowBack, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.onBackground
              )
            }
          },
          actions = {
            if (onFullScreen != null) {
              IconButton(onClick = onFullScreen) {
                Icon(
                  Icons.Rounded.OpenInFull, 
                  contentDescription = null, 
                  tint = MaterialTheme.colorScheme.onBackground
                )
              }
            }
          },
          scrollBehavior = scrollBehavior,
          modifier = Modifier.windowInsetsPadding(WindowInsets.statusBarPadding),
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
          )
        )
      },
      modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { scaffoldPadding ->
      Box(
        modifier = Modifier
          .padding(scaffoldPadding)
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background)
      ) {
        Column(
          modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          if (imageUrl.isNotEmpty()) Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(250.dp)
              .clip(MaterialTheme.shapes.extraLarge)
          ) {
            ArticleImage(imageUrl)
          }

          Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
          )

          Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
      }
    }
  }
}

@Composable
fun AudioPlayerControls(
  isPlaying: Boolean,
  currentPosition: Long,
  duration: Long,
  onPlayPause: () -> Unit,
  onSeekTo: (Long) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    // Play/Pause Button
    IconButton(onClick = onPlayPause, modifier = Modifier.size(64.dp)) {
      Icon(
        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
        contentDescription = if (isPlaying) "Pause" else "Play",
        modifier = Modifier.size(48.dp),
        tint = MaterialTheme.colorScheme.primary
      )
    }

//    // SeekBar/Slider
//    Slider(
//      value = currentPosition.toFloat(),
//      onValueChange = { position -> onSeekTo(position.toLong()) },
//      valueRange = 0f..duration.toFloat(),
//      modifier = Modifier.fillMaxWidth()
//    )
//
//    // Current Position and Duration Text
//    Row(
//      modifier = Modifier.fillMaxWidth(),
//      horizontalArrangement = Arrangement.SpaceBetween
//    ) {
////      Text(formatTime(currentPosition), style = MaterialTheme.typography.bodySmall)
////      Text(formatTime(duration), style = MaterialTheme.typography.bodySmall)
//    }
  }
}
//
//// Helper function to format time in mm:ss
//fun formatTime(millis: Long): String {
//  val totalSeconds = millis / 1000
//  val minutes = totalSeconds / 60
//  val seconds = totalSeconds % 60
//  return String.format("%02d:%02d", minutes, seconds)
//}

@Composable
fun ArticleImage(imageUrl: String) {
  val action: ImageAction by rememberImageAction(ImageRequest(imageUrl))
  val painter: Painter = rememberImageActionPainter(action)

  when (action) {
    is ImageEvent.Start -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    is ImageResult.OfError -> Icon(Icons.Rounded.Error, null, tint = MaterialTheme.colorScheme.error)
    else -> Image(
      painter = painter,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))
        .fillMaxWidth()
        .height(420.dp)
    )
  }
}
