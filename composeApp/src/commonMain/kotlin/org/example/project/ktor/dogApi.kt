package org.example.project.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable



@Serializable
data class AnimeResponse(
    val data: List<Anime>,
    val pagination: Pagination
)

@Serializable
data class Anime(
    val mal_id: Int,
    val title: String,
    val images: Images,
    val score: Double?,
    val episodes: Int?,
    val synopsis: String?
)

@Serializable
data class Images(
    val jpg: ImageUrls
)

@Serializable
data class ImageUrls(
    val image_url: String,
    val small_image_url: String,
    val large_image_url: String
)

@Serializable
data class Pagination(
    val last_visible_page: Int,
    val has_next_page: Boolean,
    val current_page: Int
)

class AnimeServiceApi(
    private val client: HttpClient
) {
    suspend fun fetchWinter2006Anime(): AnimeResponse {
        return client.get("https://api.jikan.moe/v4/seasons/2006/winter?sfw").body()
    }
}