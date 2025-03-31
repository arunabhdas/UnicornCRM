package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.unicornapp.unicorncrm.movielist.data.remote.MovieApiService
import app.unicornapp.unicorncrm.movielist.presentation.MovieDetailsViewModel
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.ui.composables.MovieDetailsItem
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import app.unicornapp.unicorncrm.movielist.domain.model.Movie

@Destination
@Composable
fun MoviesDetailScreen(
    movieId: Int,
    navController: NavController,
    navigator: DestinationsNavigator,
) {
    val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
    val movieDetailsState = movieDetailsViewModel.movieDetailState.collectAsState().value

    val backdropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApiService.IMAGE_BASE_URL + movieDetailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // Remember the scroll state for better performance
        val scrollState = rememberScrollState()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 75.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp),  // Added bottom padding for content at the bottom
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MovieDetailsItem(
                movie = movieDetailsState.movie,
                navController = navController,
            )
        }
    }
}


@Preview
@Composable
fun MoviesDetailScreenPreview() {
    MoviesDetailScreen(
        movieId = 0,
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}
