package clutchapps.deutschify.components

import clutchapps.deutschify.AndroidAudioPlayer

actual fun createAudioPlayer(): AudioPlayer {
  return AndroidAudioPlayer(appContext)
}