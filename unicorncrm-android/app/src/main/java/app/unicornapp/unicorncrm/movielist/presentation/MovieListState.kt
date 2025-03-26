package app.unicornapp.unicorncrm.movielist.presentation

import app.unicornapp.unicorncrm.movielist.domain.model.Movie

data class MovieListState (
    val isLoading: Boolean = false,
    val popularMovieListPage: Int = 0,
    val upcomingMoviewListPage: Int = 1,
    val isCurrentScreenPopular: Boolean = true,
    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList()
)