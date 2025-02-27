package com.clutchapps.deutschifynnba.androidx.compose.material3.windowsizeclass

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalWindowSizeClass: ProvidableCompositionLocal<WindowSizeClass> =
  staticCompositionLocalOf { error("No window size class provided") }

