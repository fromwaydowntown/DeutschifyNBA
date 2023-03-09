package io.github.xxfast.nytimes.components

import io.github.xxfast.nytimes.AndroidAudioPlayer

actual fun createAudioPlayer(): AudioPlayer {
  return AndroidAudioPlayer(appContext)
}