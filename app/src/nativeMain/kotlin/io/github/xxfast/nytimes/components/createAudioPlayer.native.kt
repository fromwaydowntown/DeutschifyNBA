package io.github.xxfast.nytimes.components

actual fun createAudioPlayer(): AudioPlayer {
  return NativeAudioPlayer()
}
