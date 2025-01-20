package clutchapps.deutschify.data

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import clutchapps.deutschify.di.appStorage
import clutchapps.deutschify.models.SavedArticles
import kotlinx.io.files.Path
import okio.Path.Companion.toPath

actual val store: KStore<SavedArticles> by lazy {
  storeOf(Path(path = "${appStorage}/saved.json"), default = emptySet())
}
