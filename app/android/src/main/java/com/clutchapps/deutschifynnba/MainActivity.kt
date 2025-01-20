package com.clutchapps.deutschifynnba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.defaultRouterContext
import clutchapps.deutschify.components.initializeAudioPlayer
import clutchapps.deutschify.di.appStorage
import clutchapps.deutschify.screens.home.HomeScreen
import kotlinx.io.files.Path

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    initializeAudioPlayer(this)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    val rootComponentContext: RouterContext = defaultRouterContext()
    appStorage = Path(filesDir.path)

    setContent {
      @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
      val windowSizeClass: WindowSizeClass = calculateWindowSizeClass(this)
      println(windowSizeClass)

      CompositionLocalProvider(
        LocalRouterContext provides rootComponentContext,
        com.clutchapps.deutschifynnba.androidx.compose.material3.windowsizeclass.LocalWindowSizeClass provides windowSizeClass,
      ) {
        DeutschifyTheme {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
          ) {
            HomeScreen()
          }
        }
      }
    }
  }
}
