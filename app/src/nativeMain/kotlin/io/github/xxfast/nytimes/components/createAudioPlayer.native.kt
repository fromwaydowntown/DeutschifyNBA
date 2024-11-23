package clutchapps.deutschify.components

actual fun createAudioPlayer(): AudioPlayer {
  return NativeAudioPlayer()
}
