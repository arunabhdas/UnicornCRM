package app.unicornapp.unicorncrm.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.unicornapp.unicorncrm.movielist.domain.model.Movie


@Composable
fun MovieCard(
    movie: Movie,
    onMovieClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMovieClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = movie.title)
            Text(text = "Symbol: ${movie.overview}")
        }
    }
}

