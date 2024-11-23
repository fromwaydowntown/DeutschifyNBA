package clutchapps.deutschify// androidMain/kotlin/AndroidAudioPlayer.kt

import android.content.Context
import android.media.MediaPlayer
import clutchapps.deutschify.components.AudioPlayer
import java.io.File

class AndroidAudioPlayer(private val context: Context) : AudioPlayer {
  private var mediaPlayer: MediaPlayer? = null
  private var tempFile: File? = null

  override suspend fun play(data: ByteArray) {
    stop() // Stop any existing playback
    tempFile = File.createTempFile("audio", ".mp3", context.cacheDir).apply {
      writeBytes(data)
    }
    mediaPlayer = MediaPlayer().apply {
      setDataSource(tempFile!!.absolutePath)
      prepare()
      start()
    }
  }

  override fun pause() {
    mediaPlayer?.pause()
  }

  override fun stop() {
    mediaPlayer?.stop()
    mediaPlayer?.release()
    mediaPlayer = null
    tempFile?.delete()
    tempFile = null
  }

  override fun seekTo(position: Long) {
    mediaPlayer?.seekTo(position.toInt())
  }

  override val isPlaying: Boolean
    get() = mediaPlayer?.isPlaying ?: false

  override val currentPosition: Long
    get() = mediaPlayer?.currentPosition?.toLong() ?: 0L

  override val duration: Long
    get() = mediaPlayer?.duration?.toLong() ?: 0L
}


