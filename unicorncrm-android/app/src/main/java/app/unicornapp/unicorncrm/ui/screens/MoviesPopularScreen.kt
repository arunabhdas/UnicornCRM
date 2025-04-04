package app.unicornapp.unicorncrm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.unicornapp.unicorncrm.movielist.presentation.MovieListUiEvent
import app.unicornapp.unicorncrm.movielist.presentation.MoviesViewModel
import app.unicornapp.unicorncrm.movielist.util.Category
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.ui.composables.PullToRefreshLazyColumn
import app.unicornapp.unicorncrm.ui.navigation.ScreenDrawer
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import app.unicornapp.unicorncrm.ui.composables.MovieCard
import app.unicornapp.unicorncrm.ui.composables.MovieCardItem
import app.unicornapp.unicorncrm.ui.composables.PullToRefreshLazyVerticalGrid
import app.unicornapp.unicorncrm.ui.composables.PullToRefreshLazyVerticalGridIndexed
import timber.log.Timber

@Destination
@Composable
fun MoviesPopularScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
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
        // Add a title at the top of the screen
        Text(
            text = "Popular",
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp)
                .align(Alignment.TopCenter),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        if (movieListState.popularMovieList.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            PullToRefreshLazyVerticalGridIndexed(
                items = movieListState.popularMovieList,
                content = { index, movie ->
                    MovieCardItem(
                        movie = movie,
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val isNearEnd = index >= movieListState.popularMovieList.size - 1
                    if (isNearEnd && !movieListState.isLoading) {
                        Timber.d("---MoviesPopularScreen about to call movieListViewModel.onEvent---")
                        // TODO-FIXME-CLEANUP-IMPROVE onEvent(MovieListUiEvent.Paginate(Category.POPULAR))
                        movieListViewModel.onEvent(MovieListUiEvent.Paginate(Category.POPULAR))
                    }
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



@Preview
@Composable
fun MoviesPopularScreenPreview() {
   MoviesPopularScreen(
       navController = rememberNavController(),
       navigator = MockDestinationsNavigator()
   )
}

