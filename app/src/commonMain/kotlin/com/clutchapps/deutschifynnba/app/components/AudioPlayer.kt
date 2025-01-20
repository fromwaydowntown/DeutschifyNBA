package clutchapps.deutschify.components

interface AudioPlayer {
    suspend fun play(data: ByteArray)
    fun pause()
    fun stop()
    fun seekTo(position: Long)
    val isPlaying: Boolean
    val currentPosition: Long
    val duration: Long
}
