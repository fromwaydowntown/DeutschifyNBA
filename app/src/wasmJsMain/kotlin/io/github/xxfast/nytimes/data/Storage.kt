package clutchapps.deutschify.data

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import clutchapps.deutschify.models.SavedArticles

actual val store: KStore<SavedArticles> by lazy {
  storeOf(key = "saved", default = emptySet())
}
