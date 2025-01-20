package clutchapps.deutschify.data

import io.github.xxfast.kstore.KStore
import clutchapps.deutschify.models.SavedArticles

expect val store: KStore<SavedArticles>
