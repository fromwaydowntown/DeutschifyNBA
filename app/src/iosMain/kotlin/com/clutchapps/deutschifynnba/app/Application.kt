package clutchapps.deutschify

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import clutchapps.deutschify.di.appStorage
import clutchapps.deutschify.screens.home.HomeScreen
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.io.files.Path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSStringFromClass
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIApplicationMain
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun main() {
  val args = emptyArray<String>()
  memScoped {
    val argc = args.size + 1
    val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
    autoreleasepool {
      UIApplicationMain(argc, argv, null, NSStringFromClass(AppDelegate))
    }
  }
}

@OptIn(
  ExperimentalDecomposeApi::class,
  ExperimentalMaterial3WindowSizeClassApi::class,
  ExperimentalForeignApi::class
)
fun HomeUIViewController(routerContext: RouterContext): UIViewController {
  val fileManager:NSFileManager = NSFileManager.defaultManager
  val documentsUrl: NSURL? = fileManager.URLForDirectory(
    directory = NSDocumentDirectory,
    appropriateForURL = null,
    create = false,
    inDomain = NSUserDomainMask,
    error = null
  )

  val path: String = requireNotNull(documentsUrl?.path) { "Documents directory not found" }
  appStorage = Path(path)

  return ComposeUIViewController {
    /**
     * TODO: Maybe we can use [LocalUIViewController], but there's no real way to hook into [ComposeWindow.viewDidLoad]
     * */
    BoxWithConstraints {
      val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()
      CompositionLocalProvider(
        LocalRouterContext provides routerContext,
        com.clutchapps.deutschifynnba.androidx.compose.material3.windowsizeclass.LocalWindowSizeClass provides windowSizeClass,
      ) {
        MaterialTheme {
          PredictiveBackGestureOverlay(
            backDispatcher = routerContext.backHandler as BackDispatcher, // Use the same BackDispatcher as above
            backIcon = { progress, _ ->
              PredictiveBackGestureIcon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                progress = progress,
              )
            },
            modifier = Modifier.fillMaxSize(),
          ) {
            HomeScreen()
          }
        }
      }
    }
  }
}
