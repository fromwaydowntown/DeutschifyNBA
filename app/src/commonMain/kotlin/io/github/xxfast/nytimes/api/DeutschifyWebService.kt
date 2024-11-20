package io.github.xxfast.nytimes.api

import io.github.xxfast.nytimes.models.GetArticlesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.path
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class DeutschifyWebService(
  private val client: HttpClient,
  private val baseUrl: String = "/app"
) {
  suspend fun getArticles(): GetArticlesResponse =
    client.get { url { path("$baseUrl/api/articles") } }.body()

  suspend fun getArticleDetail(articleId: Int): Map<String, Any?> =
    client.get { url { path("$baseUrl/api/article/$articleId") } }.body()

  suspend fun adaptArticleText(articleId: Int, level: String): Map<String, Any?> =
    client.post { url { path("$baseUrl/api/article/$articleId/adapt") }; setBody(mapOf("level" to level)) }
      .body()

  suspend fun generateAudio(textToPlay: String): ByteArray {
    val response: HttpResponse = client.post("$baseUrl/api/play") {
      contentType(ContentType.Application.Json)
      accept(ContentType.Any) // Accept any content type, or specify ContentType.Audio.MPEG
      setBody(mapOf("text" to textToPlay))
    }

    // Check if the response status is successful
    if (!response.status.isSuccess()) {
      val errorBody = response.bodyAsText()
      throw Exception("Failed to generate audio: ${response.status}. Response: $errorBody")
    }

    // Read the response body as ByteArray
    val audioData: ByteArray = response.readBytes()

    return audioData
  }
}
