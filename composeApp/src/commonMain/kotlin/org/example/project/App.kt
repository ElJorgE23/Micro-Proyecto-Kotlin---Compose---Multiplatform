package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json



import androidx.compose.foundation.layout.*
import org.example.project.ktor.Anime

import org.example.project.ktor.AnimeServiceApi

@Composable
fun App() {
    MaterialTheme {
        val client = remember {
            HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
        val animeService = remember { AnimeServiceApi(client) }
        var animeList by remember { mutableStateOf<List<Anime>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            try {
                val response = animeService.fetchWinter2006Anime()
                animeList = response.data
                isLoading = false
            } catch (e: Exception) {
                error = e.message
                isLoading = false
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(
                text = "Winter 2006 Anime Season",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isLoading -> Text("Loading anime...")
                error != null -> Text("Error: $error")
                animeList.isEmpty() -> Text("No anime found")
                else -> LazyColumn {
                    items(animeList) { anime ->
                        AnimeCard(anime = anime)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeCard(anime: Anime) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = anime.title,
                style = MaterialTheme.typography.titleLarge
            )

            anime.score?.let { score ->
                Text(
                    text = "Score: $score",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            anime.episodes?.let { episodes ->
                Text(
                    text = "Episodes: $episodes",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            KamelImage(
                resource = asyncPainterResource(anime.images.jpg.image_url),
                contentDescription = anime.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Fit
            )

            anime.synopsis?.let { synopsis ->
                Text(
                    text = synopsis,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}