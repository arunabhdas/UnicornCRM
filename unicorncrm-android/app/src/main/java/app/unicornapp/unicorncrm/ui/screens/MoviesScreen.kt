package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import app.unicornapp.unicorncrm.movielist.domain.model.Movie
import app.unicornapp.unicorncrm.movielist.presentation.MoviesViewModel
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.ui.composables.PullToRefreshLazyColumn
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import app.unicornapp.unicorncrm.ui.screens.destinations.MoviesDetailScreenDestination
import androidx.compose.foundation.clickable
import app.unicornapp.unicorncrm.ui.navigation.ScreenDrawer

@Destination
@Composable
fun MoviesScreen(
    navController: NavController,
    navigator: DestinationsNavigator

) {
    val movieListViewModel = hiltViewModel<MoviesViewModel>()
    val movieListState by movieListViewModel.movieListState.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (movieListState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            PullToRefreshLazyColumn(
                items = movieListState.popularMovieList,
                content = { movie ->
                    MovieCard(
                        movie = movie,
                        onMovieClick = {
                            navController.navigate(
                                ScreenDrawer.MoviesDetailScreen.route.replace(
                                    "{movieId}",
                                    movie.id.toString()
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                },
                isRefreshing = movieListState.isLoading,
                onRefresh = { movieListViewModel.refreshMovies() },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 56.dp)
            )
        }
    }
}

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



@Preview
@Composable
fun MoviesScreenPreview() {
   MoviesScreen(
       navController = rememberNavController(),
       navigator = MockDestinationsNavigator()
   )
}

