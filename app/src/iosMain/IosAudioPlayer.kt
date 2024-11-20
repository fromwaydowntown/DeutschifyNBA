import platform.AVFoundation.*
import platform.Foundation.*
import kotlinx.cinterop.*
import io.github.xxfast.nytimes.components.AudioPlayer

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
class IosAudioPlayer : AudioPlayer {
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
            val writeErrorPtr = memScoped { alloc<ObjCObjectVar<NSError?>>() }
            val success = nsData.writeToURL(tempFile!!, options = 0, error = writeErrorPtr.ptr)
            if (!success) {
                val writeError = writeErrorPtr.value
                throw Exception("Error writing NSData to file: ${writeError?.localizedDescription}")
            }

            // Initialize and play audio
            audioPlayer = memScoped {
                val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                val player = AVAudioPlayer(contentsOfURL = tempFile!!, error = errorPtr.ptr)
                val error = errorPtr.value
                if (error != null) {
                    throw Exception("Error initializing AVAudioPlayer: ${error.localizedDescription}")
                }
                player
            }

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
                val removeErrorPtr = memScoped { alloc<ObjCObjectVar<NSError?>>() }
                NSFileManager.defaultManager.removeItemAtURL(it, error = removeErrorPtr.ptr)
                val removeError = removeErrorPtr.value
                if (removeError != null) {
                    println("Error deleting temporary file: ${removeError.localizedDescription}")
                }
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
}

actual fun createAudioPlayer(): AudioPlayer = IosAudioPlayer()