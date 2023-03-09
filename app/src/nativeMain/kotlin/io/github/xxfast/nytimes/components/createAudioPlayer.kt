package io.github.xxfast.nytimes.components

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.reinterpret
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import platform.Foundation.temporaryDirectory
import platform.Foundation.writeToURL
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Exception
import kotlin.Long
import kotlin.OptIn
import kotlin.let
import kotlin.toULong

@OptIn(ExperimentalForeignApi::class)
class NativeAudioPlayer : AudioPlayer {
  private var audioPlayer: AVAudioPlayer? = null
  private var tempFile: NSURL? = null

  override suspend fun play(data: ByteArray) {
    try {
      stop() // Stop any existing playback

      // Create temporary file
      val tempDir = NSFileManager.defaultManager.temporaryDirectory
      tempFile = tempDir.URLByAppendingPathComponent("audio.mp3")

      // Write data to file
      val nsData = data.toNSData()
      nsData.writeToURL(tempFile!!, atomically = true)

      // Initialize and play audio
      audioPlayer = AVAudioPlayer(
        contentsOfURL = tempFile!!,
        error = null
      )

      audioPlayer?.prepareToPlay()
      audioPlayer?.play()
    } catch (e: Exception) {
      println("Error playing audio: ${e.message}")
      // Clean up if there's an error
      stop()
    }
  }

  override fun pause() {
    try {
      audioPlayer?.pause()
    } catch (e: Exception) {
      println("Error pausing audio: ${e.message}")
    }
  }

  override fun stop() {
    try {
      audioPlayer?.stop()
      audioPlayer = null

      // Delete temporary file
      tempFile?.let {
        NSFileManager.defaultManager.removeItemAtURL(it, null)
      }
      tempFile = null
    } catch (e: Exception) {
      println("Error stopping audio: ${e.message}")
    }
  }

  override fun seekTo(position: Long) {
    try {
      audioPlayer?.currentTime = position.toDouble() / 1000.0
    } catch (e: Exception) {
      println("Error seeking audio: ${e.message}")
    }
  }

  override val isPlaying: Boolean
    get() = audioPlayer?.playing == true

  override val currentPosition: Long
    get() = (audioPlayer?.currentTime?.times(1000))?.toLong() ?: 0L

  override val duration: Long
    get() = (audioPlayer?.duration?.times(1000))?.toLong() ?: 0L

  @OptIn(ExperimentalForeignApi::class)
  private fun ByteArray.toNSData(): NSData {
    return memScoped {
      val pointer = allocArrayOf(this@toNSData)
      NSData.dataWithBytes(pointer.reinterpret(), this@toNSData.size.toULong())
    }
  }
} 