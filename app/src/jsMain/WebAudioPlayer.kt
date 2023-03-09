// jsMain/kotlin/WebAudioPlayer.kt

import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import org.w3c.dom.Audio
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

class WebAudioPlayer : AudioPlayer {
    private var audio: Audio? = null

    override suspend fun play(data: ByteArray) {
        stop() // Stop any existing playback

        val uint8Array = Uint8Array(data.toTypedArray())
        val blob = Blob(arrayOf(uint8Array), BlobPropertyBag(type = "audio/mpeg"))
        val url = window.URL.createObjectURL(blob)

        audio = Audio(url)
        audio?.play()
    }

    override fun pause() {
        audio?.pause()
    }

    override fun stop() {
        audio?.pause()
        audio?.src = ""
        audio = null
    }

    override fun seekTo(position: Long) {
        audio?.currentTime = position.toDouble() / 1000.0
    }

    override val isPlaying: Boolean
        get() = audio?.paused == false

    override val currentPosition: Long
        get() = ((audio?.currentTime ?: 0.0) * 1000).toLong()

    override val duration: Long
        get() = ((audio?.duration ?: 0.0) * 1000).toLong()
}

actual fun createAudioPlayer(): AudioPlayer = WebAudioPlayer()