package clutchapps.deutschify.components

import android.content.Context

lateinit var appContext: Context

fun initializeAudioPlayer(context: Context) {
  appContext = context.applicationContext
}
